package pl.north93.deadsimplerequestsender.plugins;

import java.io.File;

import com.google.inject.AbstractModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public final class PluginsModule extends AbstractModule
{
    private static final Logger log = LoggerFactory.getLogger(PluginsModule.class);
    private final ApplicationEnvironment applicationEnvironment;

    public PluginsModule(final ApplicationEnvironment applicationEnvironment)
    {
        this.applicationEnvironment = applicationEnvironment;
    }

    @Override
    protected void configure()
    {
        this.bind(EmptyPluginsWarning.class).asEagerSingleton();

        final PluginLoader pluginLoader = new PluginLoader();
        this.bind(PluginLoader.class).toInstance(pluginLoader);

        pluginLoader.loadPlugins(new File(this.applicationEnvironment.workingDirectory(), "plugins"));
        pluginLoader.getPlugins().forEach(this::enablePlugin);
    }

    private void enablePlugin(final DeadSimpleRequestSenderPlugin plugin)
    {
        log.info("Enabling plugin {}", plugin.getClass().getName());
        this.install(plugin);
    }
}
