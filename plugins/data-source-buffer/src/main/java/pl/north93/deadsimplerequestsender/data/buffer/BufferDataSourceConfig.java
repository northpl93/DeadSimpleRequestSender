package pl.north93.deadsimplerequestsender.data.buffer;

import com.fasterxml.jackson.annotation.JsonTypeName;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;

@JsonTypeName("buffer")
public record BufferDataSourceConfig(
        DataSourceConfig source
) implements DataSourceConfig
{
    @Override
    public DataSource createDataSource()
    {
        return new BufferDataSource(this.source.createDataSource());
    }
}
