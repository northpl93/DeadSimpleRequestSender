package pl.north93.deadsimplerequestsender.job;

import static pl.north93.deadsimplerequestsender.threading.ThreadingHelper.ensureManagementThread;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.event.JobCompletedEvent;
import pl.north93.deadsimplerequestsender.job.event.ThreadExitedEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class WorkerThreadManagement implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(WorkerThreadManagement.class);
    private final Multimap<UUID, WorkerThreadImpl> workerThreads = MultimapBuilder.hashKeys().arrayListValues().build();
    private final MessagePublisher messagePublisher;
    private final JobManagement jobManagement;

    @Inject
    public WorkerThreadManagement(final MessagePublisher messagePublisher, final JobManagement jobManagement)
    {
        this.messagePublisher = messagePublisher;
        this.jobManagement = jobManagement;
    }

    List<WorkerThreadImpl> getWorkerThreadsForJob(final UUID jobId)
    {
        ensureManagementThread();
        return new ArrayList<>(this.workerThreads.get(jobId));
    }

    void adjustWorkerThreadsForTheJob(final RunningJob runningJob)
    {
        ensureManagementThread();
        final Collection<WorkerThreadImpl> jobWorkerThreads = this.workerThreads.get(runningJob.getJobId());

        final int currentThreadCount = jobWorkerThreads.size();
        final int targetWorkerThreads = runningJob.getTargetWorkerThreads();
        if (currentThreadCount > targetWorkerThreads)
        {
            final int excessiveThreads = currentThreadCount - targetWorkerThreads;
            log.info("Destroying {} excessive threads for job {}", excessiveThreads, runningJob.getJobId());

            jobWorkerThreads.stream()
                            .limit(excessiveThreads)
                            .forEach(WorkerThreadImpl::requestTermination);
        }
        else if (currentThreadCount < targetWorkerThreads)
        {
            final int missingThreads = targetWorkerThreads - currentThreadCount;
            log.info("Spawning {} threads for job {}", missingThreads, runningJob.getJobId());

            for (int i = 0; i < missingThreads; i++)
            {
                this.createNewWorkerThread(runningJob);
            }
        }
    }

    private void createNewWorkerThread(final RunningJob runningJob)
    {
        final WorkerThreadImpl workerThread = runningJob.instantiateWorkerThread(this.messagePublisher);
        log.debug("Registering new thread {} for job {}", workerThread.getThreadId(), runningJob.getJobId());
        this.workerThreads.put(runningJob.getJobId(), workerThread);
        workerThread.start();
    }

    @Subscribe
    private void handleThreadExitedEvent(final ThreadExitedEvent threadExitedEvent)
    {
        final RunningJob runningJob = this.jobManagement.getRunningJobById(threadExitedEvent.jobId());
        if (runningJob == null)
        {
            log.warn("Received ThreadExitedEvent for non-existing job");
            return;
        }

        this.removeThreadById(runningJob.getJobId(), threadExitedEvent.threadId());
        if (runningJob.isTerminationRequested())
        {
            this.maybeTerminateJob(runningJob);
        }
        else if (runningJob.hasMoreDataToProcess())
        {
            log.info("Adjusting thread count for job {} because of thread death", runningJob.getJobId());
            this.adjustWorkerThreadsForTheJob(runningJob);
        }
        else
        {
            this.maybeCompleteJob(runningJob);
        }
    }

    private void maybeTerminateJob(final RunningJob runningJob)
    {
        final int stillRunningThreads = this.workerThreads.get(runningJob.getJobId()).size();
        if (stillRunningThreads > 0)
        {
            log.info("Waiting for shutdown of all worker threads before terminating the job");
            return;
        }

        log.info("All worker threads exited, job terminated");
        this.messagePublisher.publishEvent(new JobCompletedEvent(runningJob.getJobId()));
    }

    private void maybeCompleteJob(final RunningJob runningJob)
    {
        final int newTargetWorkerThreads = Math.max(0, runningJob.getTargetWorkerThreads() - 1);
        if (newTargetWorkerThreads > 0)
        {
            log.info("Reducing target worker thread count for job {} to {} because there is no more data to process", runningJob.getJobId(), newTargetWorkerThreads);
            runningJob.setTargetWorkerThreads(newTargetWorkerThreads);
        }
        else
        {
            log.info("Reducing target worker threads to 0, because the work is done");
            runningJob.setTargetWorkerThreads(0);
            this.messagePublisher.publishEvent(new JobCompletedEvent(runningJob.getJobId()));
        }
    }

    private void removeThreadById(final UUID jobId, final int threadId)
    {
        log.debug("Unregistering thread {} from job {}", threadId, jobId);
        this.workerThreads.get(jobId).removeIf(workerThread -> workerThread.getThreadId() == threadId);
    }
}
