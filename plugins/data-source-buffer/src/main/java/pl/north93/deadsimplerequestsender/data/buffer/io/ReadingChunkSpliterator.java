package pl.north93.deadsimplerequestsender.data.buffer.io;

import static java.lang.Long.MAX_VALUE;


import java.util.Spliterators;
import java.util.function.Consumer;

class ReadingChunkSpliterator extends Spliterators.AbstractSpliterator<IoDataRow>
{
    private final ReadingChunk readingChunk;

    public ReadingChunkSpliterator(final ReadingChunk readingChunk)
    {
        super(MAX_VALUE, NONNULL);
        this.readingChunk = readingChunk;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super IoDataRow> action)
    {
        final IoDataRow ioDataRow = this.readingChunk.readDataRow();
        if (ioDataRow == null)
        {
            return false;
        }

        action.accept(ioDataRow);
        return true;
    }
}
