package pl.north93.deadsimplerequestsender.messaging;

import java.util.concurrent.ExecutorService;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class EventPublisher
{
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private final ExecutorService managementExecutor;
    private final EventBus eventBus;

    EventPublisher(final ExecutorService managementExecutor, final EventBus eventBus)
    {
        this.managementExecutor = managementExecutor;
        this.eventBus = eventBus;
    }

    public void publishEvent(final Event event)
    {
        Preconditions.checkNotNull(event, "Event can't be null");
        this.managementExecutor.submit(() ->
        {
            try
            {
                log.debug("Posting an event in event bus: {}", event);
                this.eventBus.post(event);
            }
            catch (final Exception e)
            {
                log.error("Uncaught exception during event processing in management application thread", e);
            }
        });
    }
}
