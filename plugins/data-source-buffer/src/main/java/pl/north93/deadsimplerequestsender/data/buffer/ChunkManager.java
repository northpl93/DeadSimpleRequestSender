package pl.north93.deadsimplerequestsender.data.buffer;

import static pl.north93.deadsimplerequestsender.data.buffer.io.IoDataRow.toDomainDataRow;
import static pl.north93.deadsimplerequestsender.data.buffer.io.IoDataRow.toIoDataRow;
import static pl.north93.deadsimplerequestsender.data.buffer.io.ReadingChunk.createStreamFromReadingChunk;


import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.buffer.io.Io;
import pl.north93.deadsimplerequestsender.data.buffer.kryo.KryoIo;
import pl.north93.deadsimplerequestsender.data.buffer.io.ReadingChunk;
import pl.north93.deadsimplerequestsender.data.buffer.io.WritingChunk;

class ChunkManager implements Closeable
{
    private static final Logger log = LoggerFactory.getLogger(ChunkManager.class);
    private final DataHeader dataHeader;
    private final Io io;
    private final AtomicInteger chunkNumber;
    private final AtomicInteger chunksBeingWritten;
    private final Deque<WritingChunk> writingChunks;
    private final AtomicInteger chunksToRead;
    private final Deque<ReadingChunk> readingChunks;

    public ChunkManager(final DataHeader dataHeader, final File chunksDirectory)
    {
        this.dataHeader = dataHeader;
        this.io = new KryoIo(chunksDirectory);
        this.chunkNumber = new AtomicInteger(0);
        this.chunksBeingWritten = new AtomicInteger(0);
        this.writingChunks = new ConcurrentLinkedDeque<>();
        this.chunksToRead = new AtomicInteger(0);
        this.readingChunks = new ConcurrentLinkedDeque<>();
    }

    public void writeChunk(final Collection<DataRow> dataRows)
    {
        final WritingChunk writingChunk = this.acquireOrCreateChunkForWriting();
        try
        {
            for (final DataRow dataRow : dataRows)
            {
                writingChunk.writeDataRow(toIoDataRow(dataRow));
            }
        }
        finally
        {
            this.releaseChunkFromWriting(writingChunk);
        }
    }

    private WritingChunk acquireOrCreateChunkForWriting()
    {
        final WritingChunk poppedChunk = this.writingChunks.pollFirst();
        if (poppedChunk != null)
        {
            log.debug("Acquired existing chunk for writing {}", poppedChunk.name());
            return poppedChunk;
        }

        try
        {
            return this.io.openWritingChunk(this.chunkNumber.getAndIncrement());
        }
        finally
        {
            this.chunksBeingWritten.incrementAndGet();
        }
    }

    private void releaseChunkFromWriting(final WritingChunk writingChunk)
    {
        final int maxChunkSize = 1024 * 1024 * 1024;
        if (writingChunk.chunkSize() > maxChunkSize)
        {
            this.switchChunkToReadingStage(writingChunk);
            return;
        }

        log.debug("Returning chunk to write queue {}", writingChunk.name());
        this.writingChunks.addFirst(writingChunk);
    }

    private void switchChunkToReadingStage(final WritingChunk writingChunk)
    {
        log.info("Switching chunk {} to reading stage", writingChunk.name());
        // We must increment chunks to read counter first to avoid situation when
        // both counters are 0, and it looks like there are no more data to be read.
        this.chunksToRead.incrementAndGet();
        this.chunksBeingWritten.decrementAndGet();
        this.readingChunks.addFirst(writingChunk.switchToReading());
    }

    public Collection<DataRow> readData(final int batchSize)
    {
        final ReadingChunk readingChunk = this.acquireChunkForReading();
        if (readingChunk == null)
        {
            return Collections.emptyList();
        }

        try
        {
            return createStreamFromReadingChunk(readingChunk)
                         .limit(batchSize)
                         .map(binDataRow -> toDomainDataRow(this.dataHeader, binDataRow))
                         .toList();

        }
        finally
        {
            this.releaseChunkFromReading(readingChunk);
        }
    }

    private ReadingChunk acquireChunkForReading()
    {
        final ReadingChunk readingChunk = this.readingChunks.pollFirst();
        if (readingChunk != null)
        {
            log.debug("Acquired chunk {} for reading", readingChunk.name());
            return readingChunk;
        }

        // After we complete reading fully written chunks there can still be some uncompleted chunks
        // that didn't reach their target size. Now we'll switch them to reading mode.
        final WritingChunk writingChunk = this.writingChunks.pollFirst();
        if (writingChunk != null)
        {
            this.switchChunkToReadingStage(writingChunk);
        }

        return this.readingChunks.pollFirst();
    }

    private void releaseChunkFromReading(final ReadingChunk readingChunk)
    {
        if (readingChunk.pendingDataToRead() > 0)
        {
            log.debug("Returning chunk to read queue {}", readingChunk.name());
            this.readingChunks.addFirst(readingChunk);
        }
        else
        {
            log.info("Completed reading chunk {}", readingChunk.name());
            readingChunk.close();
            this.chunksToRead.decrementAndGet();
        }
    }

    public boolean hasDataToRead()
    {
        return this.chunksToRead.get() > 0 || this.chunksBeingWritten.get() > 0;
    }

    @Override
    public void close() throws IOException
    {
        WritingChunk writingChunk;
        while ((writingChunk = this.writingChunks.pollFirst()) != null)
        {
            try
            {
                writingChunk.close();
            }
            finally
            {
                this.chunksBeingWritten.decrementAndGet();
            }
        }

        ReadingChunk readingChunk;
        while ((readingChunk = this.readingChunks.pollFirst()) != null)
        {
            try
            {
                readingChunk.close();
            }
            finally
            {
                this.chunksToRead.decrementAndGet();
            }
        }
    }
}
