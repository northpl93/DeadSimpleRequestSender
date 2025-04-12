package pl.north93.deadsimplerequestsender.job;

import static pl.north93.deadsimplerequestsender.threading.ThreadingHelper.ensureManagementThread;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.event.ApplicationStandbyEvent;
import pl.north93.deadsimplerequestsender.job.event.JobCompletedEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class JobManagement implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(JobManagement.class);
    private final MessagePublisher messagePublisher;
    private final Map<UUID, RunningJob> runningJobs = new HashMap<>();

    @Inject
    JobManagement(final MessagePublisher messagePublisher)
    {
        this.messagePublisher = messagePublisher;
    }

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

    List<RunningJob> getRunningJobs()
    {
        ensureManagementThread();
        return new ArrayList<>(this.runningJobs.values());
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

        this.deleteWorkingDirectory(runningJob.getWorkDir());
        if (this.runningJobs.isEmpty())
        {
            this.messagePublisher.publishEvent(new ApplicationStandbyEvent());
        }

        log.info("Job {} completed!", runningJob.getJobId());
    }

    private void deleteWorkingDirectory(final Path workDir)
    {
        log.info("Removing job working directory {}", workDir);
        try (final Stream<Path> pathStream = Files.walk(workDir))
        {
            pathStream.sorted(Comparator.reverseOrder())
                      .map(Path::toFile)
                      .forEach(File::delete);
        }
        catch (final IOException e)
        {
            log.warn("Failed to remove job working directory {}", workDir, e);
        }
    }
}
