package pl.north93.deadsimplerequestsender.rest;

import io.javalin.Javalin;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;
import pl.north93.deadsimplerequestsender.rest.jobs.JobsResource;

public final class RestPlugin extends DeadSimpleRequestSenderPlugin
{
    @Override
    protected void configure()
    {
        this.bind(JobsResource.class);

        this.bind(Javalin.class).toProvider(JavalinProvider.class).asEagerSingleton();
        this.bind(ApiServerLifecycleListener.class).asEagerSingleton();
    }
}
