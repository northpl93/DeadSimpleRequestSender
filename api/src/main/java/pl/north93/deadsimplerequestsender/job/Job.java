package pl.north93.deadsimplerequestsender.job;

import java.nio.file.Path;
import java.util.UUID;

public interface Job
{
    UUID getJobId();

    Path getWorkDir();

    String getDisplayName();

    JobConfig getJobConfig();

    int getTargetWorkerThreads();

    boolean isTerminationRequested();
}
