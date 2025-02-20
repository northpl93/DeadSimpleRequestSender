package pl.north93.deadsimplerequestsender.application;

import java.io.File;
import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Modules;

import org.apache.commons.cli.CommandLine;

import pl.north93.deadsimplerequestsender.application.StartupModeProvider.StartupModeOptionsCustomizer;
import pl.north93.deadsimplerequestsender.data.DataSourceModule;
import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.environment.OptionsCustomizer;
import pl.north93.deadsimplerequestsender.environment.StartupMode;
import pl.north93.deadsimplerequestsender.http.apachehttpclient.RequestSenderModule;
import pl.north93.deadsimplerequestsender.job.JobModule;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisherModule;
import pl.north93.deadsimplerequestsender.plugins.PluginsModule;
import pl.north93.deadsimplerequestsender.template.StModule;
import pl.north93.deadsimplerequestsender.threading.ThreadingModule;

public final class ApplicationModule extends AbstractModule
{
    private final ApplicationEnvironment applicationEnvironment;

    public ApplicationModule(final ApplicationEnvironment applicationEnvironment)
    {
        this.applicationEnvironment = applicationEnvironment;
    }

    @Override
    protected void configure()
    {
        this.bind(ApplicationEnvironment.class).toInstance(this.applicationEnvironment);
        Multibinder.newSetBinder(this.binder(), OptionsCustomizer.class).addBinding().to(StartupModeOptionsCustomizer.class);
        this.bind(CommandLine.class).toProvider(ParsedCommandLineProvider.class);
        this.bind(StartupMode.class).toProvider(StartupModeProvider.class);
        this.bind(LaunchApplicationHandler.class).asEagerSingleton();
        this.bind(ShutdownStandbyApplication.class).asEagerSingleton();
        this.install(Modules.disableCircularProxiesModule());
        this.install(Modules.requireExplicitBindingsModule());
        this.install(new PluginsModule(this.applicationEnvironment));
        this.install(new StModule());
        this.install(new JobModule());
        this.install(new DataSourceModule());
        this.install(new RequestSenderModule());
        this.install(new ThreadingModule());
        this.install(new MessagePublisherModule());
    }

    public static ApplicationModule constructApplicationModule(final String[] args)
    {
        final ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(getWorkdir(), args);
        return new ApplicationModule(applicationEnvironment);
    }

    private static File getWorkdir()
    {
        try
        {
            return new File(".").getCanonicalFile();
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
