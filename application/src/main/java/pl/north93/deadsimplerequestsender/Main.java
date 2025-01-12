package pl.north93.deadsimplerequestsender;

import static pl.north93.deadsimplerequestsender.application.ApplicationModule.constructApplicationModule;


import com.google.inject.Guice;
import com.google.inject.Injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.application.command.LaunchApplicationCommand;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

public final class Main
{
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args)
    {
        final Injector injector = Guice.createInjector(constructApplicationModule(args));

        log.debug("Executing application launch command, initialization will continue in the management thread");
        final MessagePublisher messagePublisher = injector.getInstance(MessagePublisher.class);
        messagePublisher.executeCommand(new LaunchApplicationCommand());
    }
}
