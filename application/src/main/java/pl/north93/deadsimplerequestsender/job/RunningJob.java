package pl.north93.deadsimplerequestsender.job;

import java.io.File;
import java.util.UUID;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class RunningJob implements Job
{
    private final UUID jobId;
    private final File workDir;
    private final JobConfig jobConfig;
    private final DataSource dataSource;
    private final RequestSender requestSender;
    private int targetWorkerThreads;

    public RunningJob(
            final UUID jobId,
            final File workDir,
            final JobConfig jobConfig,
            final DataSource dataSource,
            final RequestSender requestSender,
            final int targetWorkerThreads
    )
    {
        this.jobId = jobId;
        this.workDir = workDir;
        this.jobConfig = jobConfig;
        this.dataSource = dataSource;
        this.requestSender = requestSender;
        this.targetWorkerThreads = targetWorkerThreads;
    }

    @Override
    public UUID getJobId()
    {
        return this.jobId;
    }

    public File getWorkDir()
    {
        return this.workDir;
    }

    @Override
    public JobConfig getJobConfig()
    {
        return this.jobConfig;
    }

    boolean hasMoreDataToProcess()
    {
        return this.dataSource.hasMore();
    }

    WorkerThread instantiateWorkerThread(final MessagePublisher messagePublisher)
    {
        final ThreadStateReporter threadStateReporter = new ThreadStateReporter(messagePublisher, this.jobId);
        return new WorkerThread(threadStateReporter, this.dataSource, this.requestSender);
    }

    @Override
    public int getTargetWorkerThreads()
    {
        return this.targetWorkerThreads;
    }

    public void setTargetWorkerThreads(final int targetWorkerThreads)
    {
        this.targetWorkerThreads = targetWorkerThreads;
    }
}
