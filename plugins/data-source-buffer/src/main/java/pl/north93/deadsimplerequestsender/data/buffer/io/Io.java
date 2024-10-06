package pl.north93.deadsimplerequestsender.data.buffer.io;

public interface Io
{
    WritingChunk openWritingChunk(long chunkId);
}
