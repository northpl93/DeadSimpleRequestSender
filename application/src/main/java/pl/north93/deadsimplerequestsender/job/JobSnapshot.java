package pl.north93.deadsimplerequestsender.job;

import java.nio.file.Path;
import java.util.UUID;

record JobSnapshot(
        UUID jobId,
        Path workDir,
        String displayName,
        JobConfig jobConfig,
        int targetWorkerThreads,
        boolean terminationRequested
) implements Job
{
    @Override
    public UUID getJobId()
    {
        return this.jobId;
    }

    @Override
    public Path getWorkDir()
    {
        return this.workDir;
    }

    @Override
    public String getDisplayName()
    {
        return this.displayName;
    }

    @Override
    public JobConfig getJobConfig()
    {
        return this.jobConfig;
    }

    @Override
    public int getTargetWorkerThreads()
    {
        return this.targetWorkerThreads;
    }

    @Override
    public boolean isTerminationRequested()
    {
        return this.terminationRequested;
    }

    static JobSnapshot of(final RunningJob runningJob)
    {
        return new JobSnapshot(
                runningJob.getJobId(),
                runningJob.getWorkDir(),
                runningJob.getDisplayName(),
                runningJob.getJobConfig(),
                runningJob.getTargetWorkerThreads(),
                runningJob.isTerminationRequested()
        );
    }
}
