package pl.north93.deadsimplerequestsender.plugins;

import java.io.File;

import com.google.inject.AbstractModule;

public class PluginsModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        final PluginLoader pluginLoader = new PluginLoader();
        this.binder().bind(PluginLoader.class).toInstance(pluginLoader);

        pluginLoader.loadPlugins(new File("plugins"));
        pluginLoader.getPlugins().forEach(this::install);
    }
}
