package pl.north93.deadsimplerequestsender.data.buffer;

import com.google.inject.multibindings.ProvidesIntoSet;

import pl.north93.deadsimplerequestsender.job.ObjectMapperCustomizer;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class BufferDataSourcePlugin extends DeadSimpleRequestSenderPlugin
{
    @ProvidesIntoSet
    public ObjectMapperCustomizer customizeObjectMapper()
    {
        return objectMapper -> objectMapper.registerSubtypes(BufferDataSourceConfig.class);
    }
}
