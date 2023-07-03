package pl.north93.deadsimplerequestsender.data.bigquery;

import static pl.north93.deadsimplerequestsender.data.bigquery.ArrowSchemaReader.convertArrowSchemaToDataHeader;


import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.VectorLoader;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

class DataRowReader implements Closeable
{
    private final VectorSchemaRoot vectorSchemaRoot;
    private final VectorLoader vectorLoader;
    private final DataHeader dataHeader;

    public DataRowReader(final Schema arrowSchema, final BufferAllocator bufferAllocator)
    {
        final List<FieldVector> vectors = new ArrayList<>();
        for (final Field field : arrowSchema.getFields())
        {
            vectors.add(field.createVector(bufferAllocator));
        }

        this.vectorSchemaRoot = new VectorSchemaRoot(vectors);
        this.vectorLoader = new VectorLoader(this.vectorSchemaRoot);
        this.dataHeader = convertArrowSchemaToDataHeader(arrowSchema);
    }

    public Collection<DataRow> convertArrowBatchToDataRows(final ArrowRecordBatch deserializedBatch)
    {
        try
        {
            this.vectorLoader.load(deserializedBatch);
            return this.convertVectorsToDataRows();
        }
        finally
        {
            this.vectorSchemaRoot.clear();
        }
    }

    private Collection<DataRow> convertVectorsToDataRows()
    {
        final int columnsCount = this.vectorSchemaRoot.getFieldVectors().size();
        final int rowsCount = this.vectorSchemaRoot.getRowCount();

        final DataRow[] dataRows = new DataRow[rowsCount];
        for (int i = 0; i < dataRows.length; i++)
        {
            dataRows[i] = new DataRow(this.dataHeader, new Object[columnsCount]);
        }

        for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++)
        {
            final FieldVector columnData = this.vectorSchemaRoot.getFieldVectors().get(columnIndex);
            for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++)
            {
                dataRows[rowIndex].row()[columnIndex] = columnData.getObject(rowIndex);
            }
        }

        return List.of(dataRows);
    }

    @Override
    public void close()
    {
        this.vectorSchemaRoot.close();
    }
}