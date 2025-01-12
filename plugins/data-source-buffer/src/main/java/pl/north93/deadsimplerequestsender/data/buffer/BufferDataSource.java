package pl.north93.deadsimplerequestsender.data.buffer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;

public class BufferDataSource implements DataSource
{
    private final DataSource wrappedDataSource;
    private final ChunkManager chunkManager;

    public BufferDataSource(final DataSource wrappedDataSource, final File chunksDirectory)
    {
        this.wrappedDataSource = wrappedDataSource;
        this.chunkManager = new ChunkManager(this.wrappedDataSource.readHeader(), chunksDirectory);
    }

    @Override
    public DataHeader readHeader()
    {
        return this.wrappedDataSource.readHeader();
    }

    @Override
    public Map<String, Object> metadata()
    {
        return this.wrappedDataSource.metadata();
    }

    @Override
    public boolean hasMore()
    {
        return this.wrappedDataSource.hasMore() || this.chunkManager.hasDataToRead();
    }

    @Override
    public Collection<DataRow> readBatch(final int batchSize)
    {
        if (this.wrappedDataSource.hasMore())
        {
            this.chunkManager.writeChunk(this.wrappedDataSource.readBatch(batchSize));
            return Collections.emptyList();
        }

        return this.chunkManager.readData(batchSize);
    }

    @Override
    public void close() throws IOException
    {
        this.wrappedDataSource.close();
        this.chunkManager.close();
    }
}
