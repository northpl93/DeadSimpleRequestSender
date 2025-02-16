package pl.north93.deadsimplerequestsender.job;

import java.util.List;

import com.google.inject.Inject;

import pl.north93.deadsimplerequestsender.job.command.GetJobWorkerThreadsSnapshotCommand;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;

final class GetJobWorkerThreadsSnapshotHandler implements CommandHandler<GetJobWorkerThreadsSnapshotCommand, List<? extends WorkerThread>>
{
    private final WorkerThreadManagement workerThreadManagement;

    @Inject
    GetJobWorkerThreadsSnapshotHandler(final WorkerThreadManagement workerThreadManagement)
    {
        this.workerThreadManagement = workerThreadManagement;
    }

    @Override
    public List<? extends WorkerThread> handleCommand(final GetJobWorkerThreadsSnapshotCommand command)
    {
        return this.workerThreadManagement.getWorkerThreadsForJob(command.jobId())
                                          .stream()
                                          .map(WorkerThreadSnapshot::of)
                                          .toList();
    }
}
