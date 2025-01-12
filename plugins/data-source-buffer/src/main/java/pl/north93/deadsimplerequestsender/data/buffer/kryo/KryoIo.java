package pl.north93.deadsimplerequestsender.data.buffer.kryo;

import java.io.File;
import java.io.FileNotFoundException;

import com.esotericsoftware.kryo.Kryo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.buffer.io.Io;
import pl.north93.deadsimplerequestsender.data.buffer.io.WritingChunk;

public class KryoIo implements Io
{
    private static final Logger log = LoggerFactory.getLogger(KryoIo.class);
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(this::createKryo);
    private final File chunksDirectory;

    public KryoIo(final File chunksDirectory)
    {
        this.chunksDirectory = chunksDirectory;
    }

    private Kryo createKryo()
    {
        final Kryo kryo = new Kryo();
        kryo.register(Object[].class, 9);

        return kryo;
    }

    @Override
    public WritingChunk openWritingChunk(final long chunkId)
    {
        final File file = new File(this.chunksDirectory, "chunk." + chunkId + ".bin");
        try
        {
            log.info("Opening new chunk file {}", file.getAbsolutePath());
            return new KryoWritingChunk(this.kryoThreadLocal, file);
        }
        catch (final FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
