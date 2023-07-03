package pl.north93.deadsimplerequestsender.data;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class SynchronizedDataSource implements DataSource
{
    private final DataSource dataSource;

    public SynchronizedDataSource(final DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public DataHeader readHeader()
    {
        synchronized (this)
        {
            return this.dataSource.readHeader();
        }
    }

    @Override
    public Map<String, Object> metadata()
    {
        synchronized (this)
        {
            return this.dataSource.metadata();
        }
    }

    @Override
    public boolean hasMore()
    {
        synchronized (this)
        {
            return this.dataSource.hasMore();
        }
    }

    @Override
    public Collection<DataRow> readBatch(final int batchSize)
    {
        synchronized (this)
        {
            return this.dataSource.readBatch(batchSize);
        }
    }

    @Override
    public void close() throws IOException
    {
        synchronized (this)
        {
            this.dataSource.close();
        }
    }
}
