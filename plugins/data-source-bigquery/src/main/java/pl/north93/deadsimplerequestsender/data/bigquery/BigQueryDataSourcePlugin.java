package pl.north93.deadsimplerequestsender.data.bigquery;

import com.google.inject.multibindings.ProvidesIntoSet;

import pl.north93.deadsimplerequestsender.data.bigquery.config.BigQueryTableDataSourceConfig;
import pl.north93.deadsimplerequestsender.job.ObjectMapperCustomizer;
import pl.north93.deadsimplerequestsender.plugin.DeadSimpleRequestSenderPlugin;

public class BigQueryDataSourcePlugin extends DeadSimpleRequestSenderPlugin
{
    @ProvidesIntoSet
    public ObjectMapperCustomizer customizeObjectMapper()
    {
        return objectMapper -> objectMapper.registerSubtypes(BigQueryTableDataSourceConfig.class);
    }
}
