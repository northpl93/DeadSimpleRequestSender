package pl.north93.deadsimplerequestsender.data.csv;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;
import pl.north93.deadsimplerequestsender.data.SynchronizedDataSource;

@JsonTypeName("csv")
@JsonClassDescription("CSV file")
public record CsvDataSourceConfig(String path) implements DataSourceConfig
{
    @Override
    public DataSource createDataSource(final File localWorkDir, final Injector injector)
    {
        return new SynchronizedDataSource(new CsvDataSource(new File(this.path)));
    }
}
