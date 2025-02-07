package pl.north93.deadsimplerequestsender.data;

import java.io.File;
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

    public DataSource createDataSource(final File localWorkDir, final DataSourceConfig dataSourceConfig, final List<DataPostProcessorConfig> postProcessors)
    {
        final DataSource dataSource = dataSourceConfig.createDataSource(localWorkDir, this.injector);
        if (postProcessors.isEmpty())
        {
            return dataSource;
        }

        return new PostProcessedDataSource(dataSource, postProcessors);
    }
}
