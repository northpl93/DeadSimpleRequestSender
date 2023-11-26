package pl.north93.deadsimplerequestsender.data.random;

import com.google.inject.multibindings.ProvidesIntoSet;

import pl.north93.deadsimplerequestsender.job.ObjectMapperCustomizer;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class RandomDataSourcePlugin extends DeadSimpleRequestSenderPlugin
{
    @ProvidesIntoSet
    public ObjectMapperCustomizer customizeObjectMapper()
    {
        return objectMapper -> objectMapper.registerSubtypes(RandomDataSourceConfig.class);
    }
}
