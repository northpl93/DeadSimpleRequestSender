package pl.north93.deadsimplerequestsender.plugins;

import java.io.File;

import com.google.inject.AbstractModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class PluginsModule extends AbstractModule
{
    private static final Logger log = LoggerFactory.getLogger(PluginsModule.class);

    @Override
    protected void configure()
    {
        final PluginLoader pluginLoader = new PluginLoader();
        this.binder().bind(PluginLoader.class).toInstance(pluginLoader);

        pluginLoader.loadPlugins(new File("plugins"));
        pluginLoader.getPlugins().forEach(this::enablePlugin);
    }

    private void enablePlugin(final DeadSimpleRequestSenderPlugin plugin)
    {
        log.info("Enabling plugin {}", plugin.getClass().getName());
        this.install(plugin);
    }
}
