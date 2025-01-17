package pl.north93.deadsimplerequestsender.job.command;

import pl.north93.deadsimplerequestsender.job.Job;
import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.messaging.Command;

public record SubmitJobCommand(JobConfig jobConfig) implements Command<Job>
{
}
