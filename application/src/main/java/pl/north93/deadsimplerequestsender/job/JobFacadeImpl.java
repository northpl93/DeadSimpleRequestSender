package pl.north93.deadsimplerequestsender.job;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;

import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class JobFacadeImpl implements JobFacade
{
    private final JobManagement jobManagement;
    private final MessagePublisher messagePublisher;

    @Inject
    public JobFacadeImpl(final JobManagement jobManagement, final MessagePublisher messagePublisher)
    {
        this.jobManagement = jobManagement;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public List<? extends Job> getRunningJobs()
    {
        return this.jobManagement.getRunningJobs();
    }

    @Override
    public CompletableFuture<? extends Job> submitJob(final JobConfig jobConfig)
    {
        return this.messagePublisher.executeCommand(new SubmitJobCommand(jobConfig));
    }
}
