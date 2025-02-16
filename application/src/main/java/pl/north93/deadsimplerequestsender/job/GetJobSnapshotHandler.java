package pl.north93.deadsimplerequestsender.job;

import java.util.Optional;

import com.google.inject.Inject;

import pl.north93.deadsimplerequestsender.job.command.GetJobSnapshotCommand;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;

final class GetJobSnapshotHandler implements CommandHandler<GetJobSnapshotCommand, Job>
{
    private final JobManagement jobManagement;

    @Inject
    public GetJobSnapshotHandler(final JobManagement jobManagement)
    {
        this.jobManagement = jobManagement;
    }

    @Override
    public Job handleCommand(final GetJobSnapshotCommand command)
    {
        return Optional.ofNullable(this.jobManagement.getRunningJobById(command.jobId()))
                       .map(JobSnapshot::of)
                       .orElse(null);
    }
}
