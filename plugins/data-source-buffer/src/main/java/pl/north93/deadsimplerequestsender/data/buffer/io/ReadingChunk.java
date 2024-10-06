package pl.north93.deadsimplerequestsender.data.buffer.io;

import java.io.Closeable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ReadingChunk extends Closeable
{
    String name();

    IoDataRow readDataRow();

    long pendingDataToRead();

    @Override
    void close();

    static Stream<IoDataRow> createStreamFromReadingChunk(final ReadingChunk readingChunk)
    {
        return StreamSupport.stream(new ReadingChunkSpliterator(readingChunk), false);
    }
}
