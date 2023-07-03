package pl.north93.deadsimplerequestsender.data;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;

public interface DataSource extends Closeable // todo call .close()
{
    DataHeader readHeader();

    default Map<String, Object> metadata()
    {
        return Map.of();
    }

    boolean hasMore();

    Collection<DataRow> readBatch(int batchSize);
}
