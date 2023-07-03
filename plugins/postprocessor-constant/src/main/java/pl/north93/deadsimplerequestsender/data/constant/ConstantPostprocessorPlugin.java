package pl.north93.deadsimplerequestsender.data.constant;

import com.google.inject.multibindings.ProvidesIntoSet;

import pl.north93.deadsimplerequestsender.job.ObjectMapperCustomizer;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class ConstantPostprocessorPlugin extends DeadSimpleRequestSenderPlugin
{
    @ProvidesIntoSet
    public ObjectMapperCustomizer customizeObjectMapper()
    {
        return objectMapper -> objectMapper.registerSubtypes(ConstantPostprocessorConfig.class);
    }
}
