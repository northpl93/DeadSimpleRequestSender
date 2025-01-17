package pl.north93.deadsimplerequestsender.job;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobFacade
{
    List<? extends Job> getRunningJobs();

    CompletableFuture<? extends Job> submitJob(JobConfig jobConfig);
}
