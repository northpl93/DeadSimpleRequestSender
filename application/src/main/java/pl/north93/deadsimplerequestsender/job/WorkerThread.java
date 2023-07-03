package pl.north93.deadsimplerequestsender.job;

import java.io.IOException;
import java.util.Collection;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;

public class WorkerThread extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(WorkerThread.class);
    private final DataSource dataSource;
    private final RequestSender httpClient;

    public WorkerThread(final DataSource dataSource, final RequestSender httpClient)
    {
        this.dataSource = dataSource;
        this.httpClient = httpClient;
    }

    @Override
    public void run()
    {
        log.info("Worker thread started {}", this.getName());
        try (final CloseableHttpClient httpClient = HttpClientBuilder.create().build())
        {
            while (this.dataSource.hasMore())
            {
                final Collection<DataRow> batch = this.dataSource.readBatch(2500);
                for (final DataRow dataRow : batch)
                {
                    this.httpClient.sendRequest(httpClient, dataRow);
                }
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
