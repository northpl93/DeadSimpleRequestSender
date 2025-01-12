package pl.north93.deadsimplerequestsender.integration;

import java.io.File;
import java.nio.file.Files;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import pl.north93.deadsimplerequestsender.application.command.LaunchApplicationCommand;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

public class DeadSimpleRequestSenderExtension implements ParameterResolver, BeforeAllCallback
{
    private Injector injector;

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().equals(IntegrationTestJobHelper.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return this.injector.getInstance(IntegrationTestJobHelper.class);
    }

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception
    {
        final File tempWorkdir = Files.createTempDirectory("dsrs-test-").toFile();
        this.injector = Guice.createInjector(new IntegrationTestModule(tempWorkdir));

        final MessagePublisher messagePublisher = this.injector.getInstance(MessagePublisher.class);
        messagePublisher.executeCommand(new LaunchApplicationCommand());
    }
}
