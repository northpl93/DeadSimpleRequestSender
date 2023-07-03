package pl.north93.deadsimplerequestsender.job;

import java.util.List;

import pl.north93.deadsimplerequestsender.data.DataPostProcessorConfig;
import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.PostProcessedDataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;

public class JobManagement
{
    public void submitJob(final JobConfig jobConfig)
    {
        final DataSource dataSource = this.createDataSource(jobConfig);
        final RequestSender requestSender = new RequestSender(dataSource.readHeader(), jobConfig.request());

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
