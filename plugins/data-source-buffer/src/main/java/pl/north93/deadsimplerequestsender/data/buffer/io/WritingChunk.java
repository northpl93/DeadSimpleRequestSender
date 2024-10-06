package pl.north93.deadsimplerequestsender.data.buffer.io;

import java.io.Closeable;

public interface WritingChunk extends Closeable
{
    String name();

    void writeDataRow(IoDataRow dataRow);

    long chunkSize();

    ReadingChunk switchToReading();
}
