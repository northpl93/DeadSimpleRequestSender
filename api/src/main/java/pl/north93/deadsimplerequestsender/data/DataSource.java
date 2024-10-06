package pl.north93.deadsimplerequestsender.data;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;

public interface DataSource extends Closeable // todo call .close()
{
    /**
     * Returns an object describing the header of data produced by this data source.
     *
     * <p>
     * This method may be called multiple times during the job lifecycle.
     * Every call using this method MUST return the same header.
     * There are no guarantees on which thread will call this method, an implementation
     * must properly handle concurrent access.
     */
    DataHeader readHeader();

    default Map<String, Object> metadata()
    {
        return Map.of();
    }

    /**
     * Returns a boolean indicating if this data source has more rows to be read.
     * When you return {@code false} the job is considered done.
     *
     * <p>
     * This method may be called multiple times before the next batch is rode.
     * This method is called only by job threads, an implementation must properly
     * handle concurrent access.
     */
    boolean hasMore();

    /**
     * Returns the next batch of data produced by this data source.
     * The batch should be size of a given {@code batchSize}, but it isn't required.
     * In the special case an implementation can return an empty batch even when {@link #hasMore()}
     * returns true, for example when the next batch isn't ready yet.
     *
     * <p>
     * This method id called only by job threads, an implementation must properly
     * handle concurrent access.
     */
    Collection<DataRow> readBatch(int batchSize);
}
