package pl.north93.deadsimplerequestsender.data.csv;

import com.google.inject.multibindings.ProvidesIntoSet;

import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;
import pl.north93.deadsimplerequestsender.job.ObjectMapperCustomizer;

public class CsvDataSourcePlugin extends DeadSimpleRequestSenderPlugin
{
    @ProvidesIntoSet
    public ObjectMapperCustomizer customizeObjectMapper()
    {
        return objectMapper -> objectMapper.registerSubtypes(CsvDataSourceConfig.class);
    }
}
