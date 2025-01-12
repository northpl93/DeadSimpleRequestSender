package pl.north93.deadsimplerequestsender.job;

import static pl.north93.deadsimplerequestsender.threading.ThreadingHelper.ensureManagementThread;


import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.event.JobCompletedEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;

final class JobManagement implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(JobManagement.class);
    private final Map<UUID, RunningJob> runningJobs = new HashMap<>();

    void registerJob(final RunningJob runningJob)
    {
        ensureManagementThread();
        this.runningJobs.put(runningJob.getJobId(), runningJob);
    }

    @Nullable
    RunningJob getRunningJobById(final UUID jobId)
    {
        ensureManagementThread();
        return this.runningJobs.get(jobId);
    }

    @Subscribe
    private void handleJobCompletedEvent(final JobCompletedEvent jobCompletedEvent)
    {
        final RunningJob runningJob = this.runningJobs.remove(jobCompletedEvent.jobId());
        if (runningJob == null)
        {
            log.warn("Received JobCompletedEvent for non-existing job");
            return;
        }

        log.info("Job {} completed!", runningJob.getJobId());
    }
}
