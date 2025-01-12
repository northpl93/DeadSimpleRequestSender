package pl.north93.deadsimplerequestsender.messaging;

import java.util.concurrent.CompletableFuture;

final class MessagePublisherImpl implements MessagePublisher
{
    private final EventPublisher eventPublisher;
    private final CommandExecutor commandExecutor;

    MessagePublisherImpl(final EventPublisher eventPublisher, final CommandExecutor commandExecutor)
    {
        this.eventPublisher = eventPublisher;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public <RESPONSE> CompletableFuture<RESPONSE> executeCommand(final Command<RESPONSE> command)
    {
        return this.commandExecutor.dispatchCommand(command);
    }

    @Override
    public void publishEvent(final Event event)
    {
        this.eventPublisher.publishEvent(event);
    }
}
