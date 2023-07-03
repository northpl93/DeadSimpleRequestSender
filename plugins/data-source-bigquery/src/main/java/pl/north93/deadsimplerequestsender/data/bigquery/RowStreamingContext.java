package pl.north93.deadsimplerequestsender.data.bigquery;

import static org.apache.arrow.vector.ipc.message.MessageSerializer.deserializeRecordBatch;


import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigquery.storage.v1.ReadRowsResponse;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.ipc.ReadChannel;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.arrow.vector.util.ByteArrayReadableSeekableByteChannel;

import pl.north93.deadsimplerequestsender.data.DataRow;

class RowStreamingContext implements Closeable
{
    private final String name;
    private final BufferAllocator bufferAllocator;
    private final DataRowReader dataRowReader;
    private final Iterator<ReadRowsResponse> rowsResponsesStream;

    public RowStreamingContext(final String name, final Schema arrowSchema, final ServerStream<ReadRowsResponse> rowsResponsesStream)
    {
        this.name = name;
        this.bufferAllocator = new RootAllocator(Long.MAX_VALUE);
        this.dataRowReader = new DataRowReader(arrowSchema, this.bufferAllocator);
        this.rowsResponsesStream = rowsResponsesStream.iterator();
    }

    public Collection<DataRow> readDataRowBatch()
    {
        if (! this.rowsResponsesStream.hasNext())
        {
            return Collections.emptyList();
        }

        final ReadRowsResponse readRowsResponse = this.rowsResponsesStream.next();
        try (final ArrowRecordBatch deserializedBatch = this.deserializeArrowRecordBatch(readRowsResponse.getArrowRecordBatch()))
        {
            return this.dataRowReader.convertArrowBatchToDataRows(deserializedBatch);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Failed to deserialize ArrowRecordBatch", e);
        }
    }

    private ArrowRecordBatch deserializeArrowRecordBatch(final com.google.cloud.bigquery.storage.v1.ArrowRecordBatch batch) throws IOException
    {
        final byte[] batchBytes = batch.getSerializedRecordBatch().toByteArray();
        return deserializeRecordBatch(new ReadChannel(new ByteArrayReadableSeekableByteChannel(batchBytes)), this.bufferAllocator);
    }

    @Override
    public void close()
    {
        this.dataRowReader.close();
        this.bufferAllocator.close();
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
