package pl.north93.deadsimplerequestsender.rest;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import pl.north93.deadsimplerequestsender.environment.event.ApplicationShutdownEvent;
import pl.north93.deadsimplerequestsender.environment.event.ApplicationStartupEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;

final class ApiServerLifecycleListener implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(ApiServerLifecycleListener.class);
    private final Javalin javalin;

    @Inject
    ApiServerLifecycleListener(final Javalin javalin)
    {
        this.javalin = javalin;
    }

    @Subscribe
    void handleApplicationStartupEvent(final ApplicationStartupEvent event)
    {
        log.info("Starting Javalin server");
        this.javalin.start(8080);
    }

    @Subscribe
    void handleApplicationShutdownEvent(final ApplicationShutdownEvent event)
    {
        log.info("Stopping Javalin server");
        this.javalin.stop();
    }
}
