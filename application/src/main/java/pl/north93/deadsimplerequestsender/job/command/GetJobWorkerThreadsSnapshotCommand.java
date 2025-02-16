package pl.north93.deadsimplerequestsender.job.command;

import java.util.List;
import java.util.UUID;

import pl.north93.deadsimplerequestsender.job.WorkerThread;
import pl.north93.deadsimplerequestsender.messaging.Command;

public record GetJobWorkerThreadsSnapshotCommand(UUID jobId) implements Command<List<? extends WorkerThread>>
{
}
