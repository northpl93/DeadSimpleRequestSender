package pl.north93.deadsimplerequestsender.data.buffer;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;

@JsonTypeName("buffer")
public record BufferDataSourceConfig(
        DataSourceConfig source
) implements DataSourceConfig
{
    @Override
    public DataSource createDataSource(final File localWorkDir, final Injector injector)
    {
        final File chunksDirectory = this.createAndGetChunksDirectory(localWorkDir);
        final DataSource wrappedDataSource = this.source.createDataSource(localWorkDir, injector);

        return new BufferDataSource(wrappedDataSource, chunksDirectory);
    }

    private File createAndGetChunksDirectory(final File localWorkDir)
    {
        final File chunksDirectory = new File(localWorkDir, "chunks");

        chunksDirectory.mkdirs();
        return chunksDirectory;
    }
}
