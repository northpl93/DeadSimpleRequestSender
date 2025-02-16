package pl.north93.deadsimplerequestsender.job;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountCommand;
import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountResult;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;

final class ChangeWorkerThreadCountHandler implements CommandHandler<ChangeWorkerThreadCountCommand, ChangeWorkerThreadCountResult>
{
    private static final Logger log = LoggerFactory.getLogger(ChangeWorkerThreadCountHandler.class);
    private final JobManagement jobManagement;
    private final WorkerThreadManagement workerThreadManagement;

    @Inject
    public ChangeWorkerThreadCountHandler(final JobManagement jobManagement, final WorkerThreadManagement workerThreadManagement)
    {
        this.jobManagement = jobManagement;
        this.workerThreadManagement = workerThreadManagement;
    }

    @Override
    public ChangeWorkerThreadCountResult handleCommand(final ChangeWorkerThreadCountCommand command)
    {
        final RunningJob runningJob = this.jobManagement.getRunningJobById(command.jobId());
        if (runningJob == null)
        {
            log.warn("Received ChangeWorkerThreadCountCommand for non-existing job");
            return ChangeWorkerThreadCountResult.NOT_FOUND;
        }

        if (runningJob.isTerminationRequested())
        {
            log.warn("Attempted to change worker threads count while job is being terminated");
            return ChangeWorkerThreadCountResult.JOB_IS_TERMINATING;
        }

        runningJob.setTargetWorkerThreads(command.newWorkerThreads());
        log.info("Changed worker thread count for job {} to {}", runningJob.getJobId(), runningJob.getTargetWorkerThreads());

        this.workerThreadManagement.adjustWorkerThreadsForTheJob(runningJob);
        return ChangeWorkerThreadCountResult.CHANGED;
    }
}
