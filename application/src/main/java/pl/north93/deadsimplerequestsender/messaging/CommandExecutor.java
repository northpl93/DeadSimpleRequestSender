package pl.north93.deadsimplerequestsender.messaging;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CommandExecutor
{
    private static final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers;
    private final ExecutorService managementExecutor;

    public CommandExecutor(final ExecutorService managementExecutor, final Map<Class<?>, CommandHandler<?, ?>> commandHandlers)
    {
        this.managementExecutor = managementExecutor;
        this.commandHandlers = commandHandlers;
    }

    // TODO secure against deadlocks when somebody calls CompletableFuture#get in the management thread
    public <COMMAND extends Command<RESPONSE>, RESPONSE> CompletableFuture<RESPONSE> dispatchCommand(final COMMAND command)
    {
        Preconditions.checkNotNull(command, "Command can't be null");

        final CommandHandler<COMMAND, RESPONSE> commandHandler = this.getCommandHandlerFor(command);
        final CompletableFuture<RESPONSE> completableFuture = new CompletableFuture<>();

        this.managementExecutor.submit(() ->
        {
            try
            {
                completableFuture.complete(commandHandler.handleCommand(command));
            }
            catch (final Exception e)
            {
                log.error("Uncaught exception during command execution in management application thread", e);
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @SuppressWarnings("unchecked")
    private <COMMAND extends Command<RESPONSE>, RESPONSE> CommandHandler<COMMAND, RESPONSE> getCommandHandlerFor(final COMMAND command)
    {
        final CommandHandler<COMMAND, RESPONSE> commandHandler = (CommandHandler<COMMAND, RESPONSE>) this.commandHandlers.get(command.getClass());
        if (commandHandler == null)
        {
            throw new RuntimeException("Not found command handler for command: " + command);
        }

        return commandHandler;
    }
}
