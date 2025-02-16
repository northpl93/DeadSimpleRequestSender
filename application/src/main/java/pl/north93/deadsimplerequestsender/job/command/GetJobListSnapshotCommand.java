package pl.north93.deadsimplerequestsender.job.command;

import java.util.List;

import pl.north93.deadsimplerequestsender.job.Job;
import pl.north93.deadsimplerequestsender.messaging.Command;

public record GetJobListSnapshotCommand() implements Command<List<? extends Job>>
{
}
