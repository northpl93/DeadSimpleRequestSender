package pl.north93.deadsimplerequestsender.data.buffer.kryo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import pl.north93.deadsimplerequestsender.data.buffer.io.IoDataRow;
import pl.north93.deadsimplerequestsender.data.buffer.io.WritingChunk;

class KryoWritingChunk implements WritingChunk
{
    private final ThreadLocal<Kryo> kryoThreadLocal;
    private final File file;
    private final Output output;

    public KryoWritingChunk(final ThreadLocal<Kryo> kryoThreadLocal, final File file) throws FileNotFoundException
    {
        this.kryoThreadLocal = kryoThreadLocal;
        this.file = file;
        this.output = new Output(new FileOutputStream(file));
    }

    @Override
    public String name()
    {
        return this.file.getName();
    }

    @Override
    public void writeDataRow(final IoDataRow dataRow)
    {
        this.kryoThreadLocal.get().writeObject(this.output, dataRow.row());
    }

    @Override
    public long chunkSize()
    {
        return this.output.total();
    }

    @Override
    public KryoReadingChunk switchToReading()
    {
        this.output.close();
        try
        {
            return new KryoReadingChunk(this.kryoThreadLocal, this.file);
        }
        catch (final FileNotFoundException e)
        {
            throw new IllegalStateException("File doesn't exist, but it was written while ago.", e);
        }
    }

    @Override
    public String toString()
    {
        return this.file.getAbsolutePath();
    }

    @Override
    public void close()
    {
        this.output.close();
    }
}
