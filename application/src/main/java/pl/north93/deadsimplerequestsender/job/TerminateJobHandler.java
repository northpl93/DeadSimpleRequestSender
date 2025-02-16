package pl.north93.deadsimplerequestsender.job;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.command.TerminateJobCommand;
import pl.north93.deadsimplerequestsender.job.command.TerminateJobResult;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;

final class TerminateJobHandler implements CommandHandler<TerminateJobCommand, TerminateJobResult>
{
    private static final Logger log = LoggerFactory.getLogger(TerminateJobHandler.class);
    private final JobManagement jobManagement;
    private final WorkerThreadManagement workerThreadManagement;

    @Inject
    public TerminateJobHandler(final JobManagement jobManagement, final WorkerThreadManagement workerThreadManagement)
    {
        this.jobManagement = jobManagement;
        this.workerThreadManagement = workerThreadManagement;
    }

    @Override
    public TerminateJobResult handleCommand(final TerminateJobCommand command)
    {
        final RunningJob runningJob = this.jobManagement.getRunningJobById(command.jobId());
        if (runningJob == null)
        {
            return TerminateJobResult.NOT_FOUND;
        }

        log.info("Terminating job {}", runningJob.getJobId());
        runningJob.requestTermination();
        this.workerThreadManagement.adjustWorkerThreadsForTheJob(runningJob);

        return TerminateJobResult.TERMINATION_REQUESTED;
    }
}
