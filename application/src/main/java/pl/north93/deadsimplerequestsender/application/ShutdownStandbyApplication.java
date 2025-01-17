package pl.north93.deadsimplerequestsender.application;

import java.util.concurrent.ExecutorService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.environment.StartupMode.SingleShotMode;
import pl.north93.deadsimplerequestsender.environment.event.ApplicationShutdownEvent;
import pl.north93.deadsimplerequestsender.job.event.ApplicationStandbyEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class ShutdownStandbyApplication implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(ShutdownStandbyApplication.class);
    private final ApplicationEnvironment applicationEnvironment;
    private final MessagePublisher messagePublisher;
    private final ExecutorService executorService;

    @Inject
    ShutdownStandbyApplication(final ApplicationEnvironment applicationEnvironment, final MessagePublisher messagePublisher, final ExecutorService executorService)
    {
        this.applicationEnvironment = applicationEnvironment;
        this.messagePublisher = messagePublisher;
        this.executorService = executorService;
    }

    @Subscribe
    private void handleApplicationStandbyEvent(final ApplicationStandbyEvent event)
    {
        if (this.applicationEnvironment.startupMode() instanceof SingleShotMode)
        {
            log.info("Shutting down application because all jobs were completed");
            this.messagePublisher.publishEvent(new ApplicationShutdownEvent());
            this.executorService.shutdown();
        }
    }
}
