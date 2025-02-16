package pl.north93.deadsimplerequestsender.job;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountResult;
import pl.north93.deadsimplerequestsender.job.command.TerminateJobResult;

public interface JobFacade
{
    CompletableFuture<List<? extends Job>> getRunningJobs();

    CompletableFuture<? extends Job> getRunningJob(UUID jobId);

    CompletableFuture<? extends Job> submitJob(JobConfig jobConfig);

    CompletableFuture<ChangeWorkerThreadCountResult> updateTargetWorkerThreadsCount(UUID jobId, int workerThreadsCount);

    CompletableFuture<TerminateJobResult> terminateJob(UUID jobId);

    CompletableFuture<List<? extends WorkerThread>> getWorkerThreads(UUID jobId);
}
