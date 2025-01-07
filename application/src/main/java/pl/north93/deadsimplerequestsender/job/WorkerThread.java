package pl.north93.deadsimplerequestsender.job;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSendingException;

public final class WorkerThread extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(WorkerThread.class);
    private final DataSource dataSource;
    private final RequestSender requestSender;

    public WorkerThread(final DataSource dataSource, final RequestSender requestSender)
    {
        this.dataSource = dataSource;
        this.requestSender = requestSender;
    }

    @Override
    public void run()
    {
        log.info("Worker thread started {}", this.getName());
        while (this.dataSource.hasMore())
        {
            final Collection<DataRow> batch = this.dataSource.readBatch(2500);
            for (final DataRow dataRow : batch)
            {
                this.sendSingleRequest(dataRow);
            }
        }
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
