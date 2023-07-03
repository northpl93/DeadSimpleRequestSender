package pl.north93.deadsimplerequestsender.plugins;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.ServiceLoader;

import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class PluginClassLoader extends URLClassLoader
{
    public PluginClassLoader(final URL pluginFile)
    {
        super(new URL[] { pluginFile });
    }

    public Collection<DeadSimpleRequestSenderPlugin> discoverPlugins()
    {
        return ServiceLoader.load(DeadSimpleRequestSenderPlugin.class, this)
                            .stream()
                            .map(ServiceLoader.Provider::get)
                            .toList();

    }
}
