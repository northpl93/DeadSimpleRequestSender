package pl.north93.deadsimplerequestsender.job;

import java.util.List;

import com.google.inject.Inject;

import pl.north93.deadsimplerequestsender.job.command.GetJobListSnapshotCommand;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;

final class GetJobListSnapshotHandler implements CommandHandler<GetJobListSnapshotCommand, List<? extends Job>>
{
    private final JobManagement jobManagement;

    @Inject
    public GetJobListSnapshotHandler(final JobManagement jobManagement)
    {
        this.jobManagement = jobManagement;
    }

    @Override
    public List<? extends Job> handleCommand(final GetJobListSnapshotCommand command)
    {
        return this.jobManagement.getRunningJobs().stream().map(JobSnapshot::of).toList();
    }
}
