package pl.north93.deadsimplerequestsender.job;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
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
    private boolean terminationRequested;
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

    @Override
    public String getDisplayName()
    {
        return Optional.ofNullable(this.jobConfig.displayName())
                       .orElseGet(this.jobId::toString);
    }

    @Override
    public Path getWorkDir()
    {
        return this.workDir.toPath();
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

    WorkerThreadImpl instantiateWorkerThread(final MessagePublisher messagePublisher)
    {
        final ThreadStateReporter threadStateReporter = new ThreadStateReporter(messagePublisher, this.jobId);
        return new WorkerThreadImpl(threadStateReporter, this.dataSource, this.requestSender);
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

    @Override
    public boolean isTerminationRequested()
    {
        return this.terminationRequested;
    }

    public void requestTermination()
    {
        this.targetWorkerThreads = 0;
        this.terminationRequested = true;
    }
}
