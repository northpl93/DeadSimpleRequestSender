package pl.north93.deadsimplerequestsender.rest.jobs;

import java.util.Optional;
import java.util.UUID;

import com.google.inject.Inject;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import pl.north93.deadsimplerequestsender.job.Job;
import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.job.JobFacade;
import pl.north93.deadsimplerequestsender.job.YamlJobConfigLoader;
import pl.north93.deadsimplerequestsender.rest.jobs.dto.PatchJobDto;

public final class JobsResource
{
    private final YamlJobConfigLoader yamlJobConfigLoader;
    private final JobFacade jobFacade;

    @Inject
    JobsResource(final YamlJobConfigLoader yamlJobConfigLoader, final JobFacade jobFacade)
    {
        this.yamlJobConfigLoader = yamlJobConfigLoader;
        this.jobFacade = jobFacade;
    }

    public void createNewJob(final Context ctx) throws Exception
    {
        final JobConfig jobConfig = ctx.bodyAsClass(JobConfig.class);
        final Job job = this.jobFacade.submitJob(jobConfig).get();

        ctx.json(job);
    }

    public void getRunningJobs(final Context ctx) throws Exception
    {
        ctx.json(this.jobFacade.getRunningJobs().get());
    }

    public void getJob(final Context ctx) throws Exception
    {
        final Job job = this.getJobOrThrow(ctx);
        ctx.json(job);
    }

    public void generateYamlRepresentation(final Context ctx) throws Exception
    {
        final Job job = this.getJobOrThrow(ctx);
        ctx.result(this.yamlJobConfigLoader.jobConfigToYaml(job.getJobConfig()));
    }

    public void killJob(final Context ctx) throws Exception
    {
        final Job job = this.getJobOrThrow(ctx);
        this.jobFacade.terminateJob(job.getJobId()).get();
    }

    public void patchJob(final Context ctx) throws Exception
    {
        final Job job = this.getJobOrThrow(ctx);

        final PatchJobDto patchJobDto = ctx.bodyAsClass(PatchJobDto.class);
        if (patchJobDto.targetWorkerThreads() != null)
        {
            this.jobFacade.updateTargetWorkerThreadsCount(job.getJobId(), patchJobDto.targetWorkerThreads());
        }
    }

    public void getWorkerThreads(final Context ctx) throws Exception
    {
        final Job job = this.getJobOrThrow(ctx);
        ctx.json(this.jobFacade.getWorkerThreads(job.getJobId()).get());
    }

    private Job getJobOrThrow(final Context ctx) throws Exception
    {
        final UUID jobId = UUID.fromString(ctx.pathParam("id"));
        return Optional.ofNullable(this.jobFacade.getRunningJob(jobId).get())
                       .orElseThrow(NotFoundResponse::new);
    }
}
