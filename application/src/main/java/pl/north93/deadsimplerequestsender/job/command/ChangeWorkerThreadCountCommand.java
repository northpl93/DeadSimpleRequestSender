package pl.north93.deadsimplerequestsender.job.command;

import java.util.UUID;

import pl.north93.deadsimplerequestsender.messaging.Command;

public record ChangeWorkerThreadCountCommand(UUID jobId, int newWorkerThreads) implements Command<Boolean>
{
}
