package pl.north93.deadsimplerequestsender.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class PluginLoader
{
    private final Collection<DeadSimpleRequestSenderPlugin> plugins = new ArrayList<>();

    public void loadPlugins(final File pluginsDirectory)
    {
        for (final File file : pluginsDirectory.listFiles())
        {
            if (! file.isFile())
            {
                continue;
            }

            final PluginClassLoader pluginClassLoader = new PluginClassLoader(fileToUrl(file));
            this.plugins.addAll(pluginClassLoader.discoverPlugins());
        }
    }

    public Collection<DeadSimpleRequestSenderPlugin> getPlugins()
    {
        return Collections.unmodifiableCollection(this.plugins);
    }

    private static URL fileToUrl(final File file)
    {
        try
        {
            return file.toURI().toURL();
        }
        catch (final MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
