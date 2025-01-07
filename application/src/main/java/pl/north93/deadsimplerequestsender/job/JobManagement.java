package pl.north93.deadsimplerequestsender.job;

import java.util.List;

import pl.north93.deadsimplerequestsender.data.DataPostProcessorConfig;
import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.PostProcessedDataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSenderFactory;

public class JobManagement
{
    private final RequestSenderFactory requestSenderFactory;

    public JobManagement(final RequestSenderFactory requestSenderFactory)
    {
        this.requestSenderFactory = requestSenderFactory;
    }

    public void submitJob(final JobConfig jobConfig)
    {
        final DataSource dataSource = this.createDataSource(jobConfig);
        final RequestSender requestSender = this.requestSenderFactory.createRequestSender(dataSource.readHeader(), jobConfig);

        for (int i = 0; i < jobConfig.executor().threads(); i++)
        {
            new WorkerThread(dataSource, requestSender).start();
        }
    }

    private DataSource createDataSource(final JobConfig jobConfig)
    {
        final DataSource dataSource = jobConfig.data().createDataSource();
        final List<DataPostProcessorConfig> dataPostProcessors = jobConfig.postProcessors();
        if (dataPostProcessors.isEmpty())
        {
            return dataSource;
        }

        return new PostProcessedDataSource(dataSource, dataPostProcessors);
    }
}
