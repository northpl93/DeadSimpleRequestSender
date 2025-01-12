package pl.north93.deadsimplerequestsender.messaging;

import java.util.concurrent.CompletableFuture;

public interface MessagePublisher
{
    <RESPONSE> CompletableFuture<RESPONSE> executeCommand(Command<RESPONSE> command);

    void publishEvent(Event event);
}
