package pl.north93.deadsimplerequestsender.data.csv;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonTypeName;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;
import pl.north93.deadsimplerequestsender.data.SynchronizedDataSource;

@JsonTypeName("csv")
public record CsvDataSourceConfig(String path) implements DataSourceConfig
{
    @Override
    public DataSource createDataSource()
    {
        return new SynchronizedDataSource(new CsvDataSource(new File(this.path)));
    }
}
