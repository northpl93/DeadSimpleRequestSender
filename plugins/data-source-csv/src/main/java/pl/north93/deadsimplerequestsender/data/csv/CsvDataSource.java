package pl.north93.deadsimplerequestsender.data.csv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataSource;

public class CsvDataSource implements DataSource
{
    private final CSVParser parser;

    public CsvDataSource(final File file)
    {
        final CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build();
        try
        {
            this.parser = new CSVParser(new FileReader(file), csvFormat);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Failed to create CSV parser, file exception occurred", e);
        }
    }

    @Override
    public DataHeader readHeader()
    {
        return new DataHeader(this.parser.getHeaderNames().toArray(new String[0]));
    }

    @Override
    public boolean hasMore()
    {
        return this.parser.iterator().hasNext();
    }

    @Override
    public Collection<DataRow> readBatch(final int batchSize)
    {
        final DataHeader dataHeader = this.readHeader();
        final Iterator<CSVRecord> iterator = this.parser.iterator();

        final Collection<DataRow> batch = new ArrayList<>(batchSize);
        while (iterator.hasNext() && batch.size() < batchSize)
        {
            batch.add(new DataRow(dataHeader, iterator.next().values()));
        }

        return batch;
    }

    @Override
    public void close() throws IOException
    {
        this.parser.close();
    }
}
