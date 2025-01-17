package pl.north93.deadsimplerequestsender.job;

import java.util.UUID;

public interface Job
{
    UUID getJobId();

    JobConfig getJobConfig();

    int getTargetWorkerThreads();
}
