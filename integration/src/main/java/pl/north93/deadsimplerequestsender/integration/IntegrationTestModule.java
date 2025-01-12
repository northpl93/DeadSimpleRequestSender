package pl.north93.deadsimplerequestsender.integration;

import java.io.File;

import com.google.inject.AbstractModule;

import pl.north93.deadsimplerequestsender.application.ApplicationModule;
import pl.north93.deadsimplerequestsender.data.buffer.BufferDataSourcePlugin;
import pl.north93.deadsimplerequestsender.data.csv.CsvDataSourcePlugin;
import pl.north93.deadsimplerequestsender.data.random.RandomDataSourcePlugin;
import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.environment.StartupMode.DaemonMode;
import pl.north93.deadsimplerequestsender.http.inline.InlineBodyFactoryPlugin;

public final class IntegrationTestModule extends AbstractModule
{
    private final File workingDirectory;

    public IntegrationTestModule(final File workingDirectory)
    {
        this.workingDirectory = workingDirectory;
    }

    @Override
    protected void configure()
    {
        this.install(new ApplicationModule(new ApplicationEnvironment(this.workingDirectory, new DaemonMode())));
        this.bind(IntegrationTestJobHelper.class);

        this.install(new InlineBodyFactoryPlugin());
        this.install(new BufferDataSourcePlugin());
        this.install(new CsvDataSourcePlugin());
        this.install(new RandomDataSourcePlugin());
    }
}
