package pl.north93.deadsimplerequestsender.data.random;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;

@JsonTypeName("random")
public record RandomDataSourceConfig(
    List<RandomColumn> columns,
    Integer limit
) implements DataSourceConfig
{
    public record RandomColumn(String name, RandomType type) {}

    public enum RandomType
    {
        STRING
                {
                    @Override
                    public Object generate()
                    {
                        return Long.toHexString(ThreadLocalRandom.current().nextLong());
                    }
                },
        INTEGER
                {
                    @Override
                    public Object generate()
                    {
                        return ThreadLocalRandom.current().nextInt();
                    }
                };

        public abstract Object generate();
    }

    @Override
    public DataSource createDataSource(final File localWorkDir, final Injector injector)
    {
        final int limit = Optional.ofNullable(this.limit).orElse(Integer.MAX_VALUE);
        return new RandomDataSource(this.columns, limit);
    }
}
