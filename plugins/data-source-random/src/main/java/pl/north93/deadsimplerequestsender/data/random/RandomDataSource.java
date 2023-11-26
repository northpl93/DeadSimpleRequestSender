package pl.north93.deadsimplerequestsender.data.random;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.random.RandomDataSourceConfig.RandomColumn;
import pl.north93.deadsimplerequestsender.data.random.RandomDataSourceConfig.RandomType;

public class RandomDataSource implements DataSource
{
    private final DataHeader dataHeader;
    private final RandomType[] columnTypes;
    private final AtomicInteger rowsLeftToGenerate;

    public RandomDataSource(final List<RandomColumn> randomColumns, final int limit)
    {
        this.dataHeader = new DataHeader(randomColumns.stream().map(RandomColumn::name).toArray(String[]::new));
        this.columnTypes = randomColumns.stream().map(RandomColumn::type).toArray(RandomType[]::new);
        this.rowsLeftToGenerate = new AtomicInteger(limit);
    }

    @Override
    public DataHeader readHeader()
    {
        return this.dataHeader;
    }

    @Override
    public boolean hasMore()
    {
        return this.rowsLeftToGenerate.get() > 0;
    }

    @Override
    public Collection<DataRow> readBatch(final int batchSize)
    {
        return Stream.generate(this::generateRandomDataRow)
                     .limit(batchSize)
                     .filter(Objects::nonNull)
                     .toList();
    }

    private DataRow generateRandomDataRow()
    {
        if (this.rowsLeftToGenerate.decrementAndGet() < 0)
        {
            return null;
        }

        final Object[] row = new Object[this.dataHeader.length()];
        for (int i = 0; i < row.length; i++)
        {
            row[i] = this.columnTypes[i].generate();
        }

        return new DataRow(this.dataHeader, row);
    }

    @Override
    public void close()
    {
    }
}
