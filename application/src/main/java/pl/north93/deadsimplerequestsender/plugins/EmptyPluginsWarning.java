package pl.north93.deadsimplerequestsender.plugins;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class EmptyPluginsWarning
{
    private static final Logger log = LoggerFactory.getLogger(EmptyPluginsWarning.class);

    @Inject
    public EmptyPluginsWarning(final PluginLoader pluginLoader)
    {
        if (pluginLoader.getPlugins().isEmpty())
        {
            log.warn("DeadSimple*RequestSender is useless without any plugins loaded");
            log.warn("Check documentation: https://github.com/northpl93/DeadSimpleRequestSender");
        }
    }
}
