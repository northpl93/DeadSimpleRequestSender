package pl.north93.deadsimplerequestsender.job;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSendingException;

final class WorkerThread extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(WorkerThread.class);
    private static final AtomicInteger WORKER_THREAD_ID = new AtomicInteger(0);
    private final UUID threadId;
    private final AtomicBoolean terminationRequested = new AtomicBoolean(false);
    private final ThreadStateReporter threadStateReporter;
    private final DataSource dataSource;
    private final RequestSender requestSender;

    public WorkerThread(final ThreadStateReporter threadStateReporter, final DataSource dataSource, final RequestSender requestSender)
    {
        super("DSRS-Worker-" + WORKER_THREAD_ID.getAndIncrement());
        this.threadId = UUID.randomUUID();
        this.threadStateReporter = threadStateReporter;
        this.dataSource = dataSource;
        this.requestSender = requestSender;
    }

    public UUID getThreadId()
    {
        return this.threadId;
    }

    public void requestTermination()
    {
        this.terminationRequested.set(true);
    }

    @Override
    public void run()
    {
        try
        {
            log.info("Worker thread started {}", this.getName());
            this.doRun();
        }
        finally
        {
            this.threadStateReporter.reportThreadExit(this);
        }
    }

    private void doRun()
    {
        while (this.hasWorkToDo())
        {
            final Collection<DataRow> batch = this.dataSource.readBatch(2500);
            for (final DataRow dataRow : batch)
            {
                this.sendSingleRequest(dataRow);
            }
        }
    }

    private boolean hasWorkToDo()
    {
        return !this.terminationRequested.get() && this.dataSource.hasMore();
    }

    private void sendSingleRequest(final DataRow dataRow)
    {
        try
        {
            this.requestSender.sendRequest(dataRow);
        }
        catch (final RequestSendingException e)
        {
            log.error("Failed to send a request {}", dataRow, e);
        }
    }
}
