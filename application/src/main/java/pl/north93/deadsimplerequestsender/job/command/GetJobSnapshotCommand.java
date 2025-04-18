package pl.north93.deadsimplerequestsender.job.command;

import java.util.UUID;

import pl.north93.deadsimplerequestsender.job.Job;
import pl.north93.deadsimplerequestsender.messaging.Command;

public record GetJobSnapshotCommand(UUID jobId) implements Command<Job>
{
}
