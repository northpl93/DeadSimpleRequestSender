package pl.north93.deadsimplerequestsender.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.CorsPluginConfig;
import pl.north93.deadsimplerequestsender.job.JsonObjectMapper;
import pl.north93.deadsimplerequestsender.rest.jobs.JobsResource;
import pl.north93.deadsimplerequestsender.rest.schema.FormSchemaResource;

final class JavalinProvider implements Provider<Javalin>
{
    private final ObjectMapper jsonObjectMapper;
    private final JobsResource jobsResource;
    private final FormSchemaResource formSchemaResource;

    @Inject
    JavalinProvider(
            final @JsonObjectMapper ObjectMapper jsonObjectMapper,
            final JobsResource jobsResource,
            final FormSchemaResource formSchemaResource
    )
    {
        this.jsonObjectMapper = jsonObjectMapper;
        this.jobsResource = jobsResource;
        this.formSchemaResource = formSchemaResource;
    }

    @Override
    public Javalin get()
    {
        final Javalin javalin = Javalin.create(config ->
        {
            config.showJavalinBanner = false;

            config.jsonMapper(new JavalinJackson(this.jsonObjectMapper, false));

            config.bundledPlugins.enableCors(corsPluginConfig ->
            {
                corsPluginConfig.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        });

        javalin.post("/jobs", this.jobsResource::createNewJob);
        javalin.get("/jobs", this.jobsResource::getRunningJobs);
        javalin.get("/jobs/{id}", this.jobsResource::getJob);
        javalin.get("/jobs/{id}/yaml", this.jobsResource::generateYamlRepresentation);
        javalin.get("/jobs/{id}/threads", this.jobsResource::getWorkerThreads);
        javalin.patch("/jobs/{id}", this.jobsResource::patchJob);
        javalin.delete("/jobs/{id}", this.jobsResource::killJob);


        javalin.get("/new-job/schema", this.formSchemaResource::getSchema);
        javalin.post("/new-job/preview", this.formSchemaResource::generateYamlRepresentation);

        return javalin;
    }
}
