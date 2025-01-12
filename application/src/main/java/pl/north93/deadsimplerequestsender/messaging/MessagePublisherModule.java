package pl.north93.deadsimplerequestsender.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.spi.ProvisionListener.ProvisionInvocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessagePublisherModule extends AbstractModule
{
    private static final Logger log = LoggerFactory.getLogger(MessagePublisherModule.class);
    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final EventBus eventBus = new EventBus();

    @Override
    protected void configure()
    {
        this.binder().bindListener(new BindSubclassOfMatcher(CommandHandler.class), this::handleCommandHandlerProvisioning);
        this.binder().bindListener(new BindSubclassOfMatcher(EventListener.class), this::handleEventListenerProvisioning);
    }

    private <T> void handleCommandHandlerProvisioning(final ProvisionInvocation<T> provision)
    {
        final CommandHandler<?, ?> provisionedObject = (CommandHandler<?, ?>) provision.provision();
        log.debug("Registering object as a command handler {}", provisionedObject);
        this.commandHandlers.put(provisionedObject.supportedCommand(), provisionedObject);
    }

    private <T> void handleEventListenerProvisioning(final ProvisionInvocation<T> provision)
    {
        final T provisionedObject = provision.provision();
        log.debug("Registering object as a event listener {}", provisionedObject);
        this.eventBus.register(provisionedObject);
    }

    @Provides
    CommandExecutor createCommandExecutor(final ExecutorService managementExecutor)
    {
        return new CommandExecutor(managementExecutor, this.commandHandlers);
    }

    @Provides
    EventPublisher createEventPublisher(final ExecutorService managementExecutor)
    {
        return new EventPublisher(managementExecutor, this.eventBus);
    }

    @Provides
    MessagePublisher createMessagePublisher(final EventPublisher eventPublisher, final CommandExecutor commandExecutor)
    {
        return new MessagePublisherImpl(eventPublisher, commandExecutor);
    }
}