package pl.north93.deadsimplerequestsender.data.bigquery;

import static java.lang.Thread.currentThread;


import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataRow;

class StreamingContextsHolder implements Closeable
{
    private static final int FREE_STREAMING_CONTEXT_AWAIT_TIME = 1_000;
    private static final Logger log = LoggerFactory.getLogger(StreamingContextsHolder.class);
    private final Deque<RowStreamingContext> streamingContexts;
    private final AtomicInteger aliveStreams;
    private final Object streamGetMonitor;

    public StreamingContextsHolder(final Collection<RowStreamingContext> streamingContexts)
    {
        log.info("Streaming data from BigQuery using {} streams", streamingContexts.size());
        this.streamingContexts = new ConcurrentLinkedDeque<>(streamingContexts);
        this.aliveStreams = new AtomicInteger(streamingContexts.size());
        this.streamGetMonitor = new Object();
    }

    public boolean hasAnyStreamingContext()
    {
        return this.aliveStreams.get() > 0;
    }

    public Collection<DataRow> readBatchFromStream(final int batchSize)
    {
        final Collection<DataRow> collectedRows = new ArrayList<>(batchSize);
        while (collectedRows.size() < batchSize)
        {
            if (this.allStreamsExhaustedAfterRead(collectedRows::addAll))
            {
                return collectedRows;
            }
        }

        return collectedRows;
    }

    private boolean allStreamsExhaustedAfterRead(final Consumer<Collection<DataRow>> batchConsumer)
    {
        RowStreamingContext rowStreamingContext;
        while ((rowStreamingContext = this.awaitFreeStreamingContext()) != null)
        {
            final Collection<DataRow> dataRowBatch = rowStreamingContext.readDataRowBatch();
            if (dataRowBatch.isEmpty())
            {
                log.info("Finished reading stream {}", rowStreamingContext);
                this.destroyStreamingContext(rowStreamingContext);
                continue;
            }

            this.releaseStreamingContext(rowStreamingContext);
            batchConsumer.accept(dataRowBatch);
            return false;
        }

        return this.aliveStreams.get() <= 0;
    }

    @Override
    public void close()
    {
        log.info("{} pending streaming contexts to close", this.aliveStreams.get());
        RowStreamingContext rowStreamingContext;
        while ((rowStreamingContext = this.awaitFreeStreamingContext()) != null)
        {
            this.destroyStreamingContext(rowStreamingContext);
        }
    }

    private RowStreamingContext awaitFreeStreamingContext()
    {
        while (this.aliveStreams.get() > 0)
        {
            final RowStreamingContext rowStreamingContext = this.streamingContexts.pollFirst();
            if (rowStreamingContext != null)
            {
                log.debug("Thread {} obtained stream {}", currentThread().getName(), rowStreamingContext);
                return rowStreamingContext;
            }

            synchronized (this.streamGetMonitor)
            {
                try
                {
                    this.streamGetMonitor.wait(FREE_STREAMING_CONTEXT_AWAIT_TIME);
                }
                catch (final InterruptedException ignored)
                {
                }
            }
        }

        return null;
    }

    private void releaseStreamingContext(final RowStreamingContext rowStreamingContext)
    {
        log.debug("Thread {} releasing stream {}", currentThread().getName(), rowStreamingContext);
        // It's important to add and poll streams from the same side of the queue because
        // otherwise, we could start reading too many streams and exhaust resources.
        this.streamingContexts.addFirst(rowStreamingContext);
        synchronized (this.streamGetMonitor)
        {
            this.streamGetMonitor.notify();
        }
    }

    private void destroyStreamingContext(final RowStreamingContext rowStreamingContext)
    {
        log.debug("Stream {} is being destroyed", rowStreamingContext);
        this.aliveStreams.decrementAndGet();
        rowStreamingContext.close();
    }

    @Override
    public String toString()
    {
        return "StreamingContextsHolder{" +
                       "streamingContexts=" + this.streamingContexts +
                       ", aliveStreams=" + this.aliveStreams +
                       '}';
    }
}
