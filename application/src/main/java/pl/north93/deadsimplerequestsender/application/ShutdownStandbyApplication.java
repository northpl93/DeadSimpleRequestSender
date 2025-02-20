package pl.north93.deadsimplerequestsender.application;

import java.util.concurrent.ExecutorService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.environment.StartupMode;
import pl.north93.deadsimplerequestsender.environment.StartupMode.SingleShotMode;
import pl.north93.deadsimplerequestsender.environment.event.ApplicationShutdownEvent;
import pl.north93.deadsimplerequestsender.job.event.ApplicationStandbyEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class ShutdownStandbyApplication implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(ShutdownStandbyApplication.class);
    private final MessagePublisher messagePublisher;
    private final ExecutorService executorService;
    private final StartupMode startupMode;

    @Inject
    ShutdownStandbyApplication(final MessagePublisher messagePublisher, final ExecutorService executorService, final StartupMode startupMode)
    {
        this.messagePublisher = messagePublisher;
        this.executorService = executorService;
        this.startupMode = startupMode;
    }

    @Subscribe
    private void handleApplicationStandbyEvent(final ApplicationStandbyEvent event)
    {
        if (this.startupMode instanceof SingleShotMode)
        {
            log.info("Shutting down application because all jobs were completed");
            this.messagePublisher.publishEvent(new ApplicationShutdownEvent());
            this.executorService.shutdown();
        }
    }
}
