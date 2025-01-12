package pl.north93.deadsimplerequestsender.data;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Injector;

public final class DataSourceFactory
{
    private final Injector injector;

    @Inject
    public DataSourceFactory(final Injector injector)
    {
        this.injector = injector;
    }

    public DataSource createDataSource(final DataSourceConfig dataSourceConfig, final List<DataPostProcessorConfig> postProcessors)
    {
        final DataSource dataSource = dataSourceConfig.createDataSource(this.injector);
        if (postProcessors.isEmpty())
        {
            return dataSource;
        }

        return new PostProcessedDataSource(dataSource, postProcessors);
    }
}
