package pl.north93.deadsimplerequestsender.data.bigquery;

import static org.apache.arrow.vector.ipc.message.MessageSerializer.deserializeSchema;


import java.io.IOException;

import com.google.cloud.bigquery.storage.v1.ArrowSchema;

import org.apache.arrow.vector.ipc.ReadChannel;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.arrow.vector.util.ByteArrayReadableSeekableByteChannel;

import pl.north93.deadsimplerequestsender.data.DataHeader;

final class ArrowSchemaReader
{
    public static Schema readArrowSchemaFromBigQuery(final ArrowSchema arrowSchema)
    {
        try
        {
            final byte[] schemaBytes = arrowSchema.getSerializedSchema().toByteArray();
            return deserializeSchema(new ReadChannel(new ByteArrayReadableSeekableByteChannel(schemaBytes)));
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static DataHeader convertArrowSchemaToDataHeader(final Schema schema)
    {
        return new DataHeader(schema.getFields().stream().map(Field::getName).toArray(String[]::new));
    }
}
