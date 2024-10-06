package pl.north93.deadsimplerequestsender.data.buffer.kryo;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.buffer.io.IoDataRow;
import pl.north93.deadsimplerequestsender.data.buffer.io.ReadingChunk;

class KryoReadingChunk implements ReadingChunk, Closeable
{
    private static final Logger log = LoggerFactory.getLogger(KryoReadingChunk.class);
    private final ThreadLocal<Kryo> kryoThreadLocal;
    private final File file;
    private final Input input;

    public KryoReadingChunk(final ThreadLocal<Kryo> kryoThreadLocal, final File file) throws FileNotFoundException
    {
        this.kryoThreadLocal = kryoThreadLocal;
        this.file = file;
        this.input = new Input(new FileInputStream(file));
    }

    @Override
    public String name()
    {
        return this.file.getName();
    }

    @Override
    public IoDataRow readDataRow()
    {
        if (this.input.end())
        {
            return null;
        }

        final Kryo kryo = this.kryoThreadLocal.get();
        return new IoDataRow(kryo.readObject(this.input, Object[].class));
    }

    @Override
    public long pendingDataToRead()
    {
        try
        {
            return this.input.available();
        }
        catch (final IOException e)
        {
            log.error("Failed to get pending data to read", e);
            return 0;
        }
    }

    @Override
    public void close()
    {
        this.input.close();
    }
}
