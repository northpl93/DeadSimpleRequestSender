package pl.north93.deadsimplerequestsender.job.command;

import java.util.UUID;

import pl.north93.deadsimplerequestsender.messaging.Command;

public record TerminateJobCommand(UUID jobId) implements Command<TerminateJobResult>
{
}
