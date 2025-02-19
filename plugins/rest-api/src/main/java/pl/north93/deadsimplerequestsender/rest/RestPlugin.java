package pl.north93.deadsimplerequestsender.rest;

import io.javalin.Javalin;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;
import pl.north93.deadsimplerequestsender.rest.jobs.JobsResource;
import pl.north93.deadsimplerequestsender.rest.schema.FormSchemaResource;
import pl.north93.deadsimplerequestsender.rest.templates.TemplateResource;
import pl.north93.deadsimplerequestsender.rest.templates.TemplatesLoader;

public final class RestPlugin extends DeadSimpleRequestSenderPlugin
{
    @Override
    protected void configure()
    {
        this.bind(JobsResource.class);

        this.bind(FormSchemaResource.class);

        this.bind(TemplatesLoader.class);
        this.bind(TemplateResource.class);

        this.bind(Javalin.class).toProvider(JavalinProvider.class).asEagerSingleton();
        this.bind(ApiServerLifecycleListener.class).asEagerSingleton();
    }
}
