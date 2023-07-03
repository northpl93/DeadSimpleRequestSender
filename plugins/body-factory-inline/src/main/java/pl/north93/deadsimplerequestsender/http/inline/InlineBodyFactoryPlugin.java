package pl.north93.deadsimplerequestsender.http.inline;

import com.google.inject.multibindings.ProvidesIntoSet;

import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;
import pl.north93.deadsimplerequestsender.job.ObjectMapperCustomizer;

public class InlineBodyFactoryPlugin extends DeadSimpleRequestSenderPlugin
{
    @ProvidesIntoSet
    public ObjectMapperCustomizer customizeObjectMapper()
    {
        return objectMapper -> objectMapper.registerSubtypes(InlineBodyFactoryConfig.class);
    }
}
