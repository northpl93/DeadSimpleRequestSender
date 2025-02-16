package pl.north93.deadsimplerequestsender.job;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;

import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountCommand;
import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountResult;
import pl.north93.deadsimplerequestsender.job.command.GetJobListSnapshotCommand;
import pl.north93.deadsimplerequestsender.job.command.GetJobSnapshotCommand;
import pl.north93.deadsimplerequestsender.job.command.GetJobWorkerThreadsSnapshotCommand;
import pl.north93.deadsimplerequestsender.job.command.TerminateJobCommand;
import pl.north93.deadsimplerequestsender.job.command.TerminateJobResult;
import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class JobFacadeImpl implements JobFacade
{
    private final MessagePublisher messagePublisher;

    @Inject
    public JobFacadeImpl(final MessagePublisher messagePublisher)
    {
        this.messagePublisher = messagePublisher;
    }

    @Override
    public CompletableFuture<List<? extends Job>> getRunningJobs()
    {
        return this.messagePublisher.executeCommand(new GetJobListSnapshotCommand());
    }

    @Override
    public CompletableFuture<? extends Job> getRunningJob(final UUID jobId)
    {
        return this.messagePublisher.executeCommand(new GetJobSnapshotCommand(jobId));
    }

    @Override
    public CompletableFuture<? extends Job> submitJob(final JobConfig jobConfig)
    {
        return this.messagePublisher.executeCommand(new SubmitJobCommand(jobConfig));
    }

    @Override
    public CompletableFuture<ChangeWorkerThreadCountResult> updateTargetWorkerThreadsCount(final UUID jobId, final int workerThreadsCount)
    {
        return this.messagePublisher.executeCommand(new ChangeWorkerThreadCountCommand(jobId, workerThreadsCount));
    }

    @Override
    public CompletableFuture<TerminateJobResult> terminateJob(final UUID jobId)
    {
        return this.messagePublisher.executeCommand(new TerminateJobCommand(jobId));
    }

    @Override
    public CompletableFuture<List<? extends WorkerThread>> getWorkerThreads(final UUID jobId)
    {
        return this.messagePublisher.executeCommand(new GetJobWorkerThreadsSnapshotCommand(jobId));
    }
}
