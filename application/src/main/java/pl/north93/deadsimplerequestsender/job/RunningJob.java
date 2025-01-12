package pl.north93.deadsimplerequestsender.job;

import java.util.UUID;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class RunningJob
{
    private final UUID jobId;
    private final DataSource dataSource;
    private final RequestSender requestSender;
    private int targetWorkerThreads;

    public RunningJob(
            final UUID jobId,
            final DataSource dataSource,
            final RequestSender requestSender,
            final int targetWorkerThreads
    )
    {
        this.jobId = jobId;
        this.dataSource = dataSource;
        this.requestSender = requestSender;
        this.targetWorkerThreads = targetWorkerThreads;
    }

    public UUID getJobId()
    {
        return this.jobId;
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

    public int getTargetWorkerThreads()
    {
        return this.targetWorkerThreads;
    }

    public void setTargetWorkerThreads(final int targetWorkerThreads)
    {
        this.targetWorkerThreads = targetWorkerThreads;
    }
}
