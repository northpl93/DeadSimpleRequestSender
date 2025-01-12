package pl.north93.deadsimplerequestsender.data.buffer;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;
import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;

@JsonTypeName("buffer")
public record BufferDataSourceConfig(
        DataSourceConfig source
) implements DataSourceConfig
{
    @Override
    public DataSource createDataSource(final Injector injector)
    {
        final File chunksDirectory = this.createAndGetChunksDirectory(injector);
        final DataSource wrappedDataSource = this.source.createDataSource(injector);

        return new BufferDataSource(wrappedDataSource, chunksDirectory);
    }

    private File createAndGetChunksDirectory(final Injector injector)
    {
        final ApplicationEnvironment applicationEnvironment = injector.getInstance(ApplicationEnvironment.class);
        final File chunksDirectory = new File(applicationEnvironment.workingDirectory(), "chunks");

        chunksDirectory.mkdirs();
        return chunksDirectory;
    }
}
