package pl.north93.deadsimplerequestsender.data;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

final class PostProcessedDataSource implements DataSource
{
    private final DataSource dataSource;
    private final List<RegisteredDataPostProcessor> dataPostProcessors;
    private final DataHeader postProcessedDataHeader;

    public PostProcessedDataSource(final DataSource dataSource, final List<DataPostProcessorConfig> dataPostProcessors)
    {
        this.dataSource = dataSource;

        final DataHeader sourceHeader = dataSource.readHeader();
        this.dataPostProcessors = dataPostProcessors.stream().map(prepareDataPostProcessor(sourceHeader)).toList();
        this.postProcessedDataHeader = prepareHeader(sourceHeader, this.dataPostProcessors);
    }

    private static Function<DataPostProcessorConfig, RegisteredDataPostProcessor> prepareDataPostProcessor(final DataHeader sourceHeader)
    {
        return dataPostProcessorConfig ->
        {
            final DataPostProcessor dataPostProcessor = dataPostProcessorConfig.createDataPostProcessor(sourceHeader);
            return new RegisteredDataPostProcessor(dataPostProcessor, dataPostProcessor.readHeader());
        };
    }

    private static DataHeader prepareHeader(final DataHeader sourceHeader, final List<RegisteredDataPostProcessor> dataPostProcessors)
    {
        return dataPostProcessors.stream()
                                 .map(RegisteredDataPostProcessor::postProcessedDataHeader)
                                 .reduce(sourceHeader, DataHeader::append);
    }

    @Override
    public DataHeader readHeader()
    {
        return this.postProcessedDataHeader;
    }

    @Override
    public Map<String, Object> metadata()
    {
        return this.dataSource.metadata();
    }

    @Override
    public boolean hasMore()
    {
        return this.dataSource.hasMore();
    }

    @Override
    public Collection<DataRow> readBatch(final int batchSize)
    {
        return this.dataSource.readBatch(batchSize)
                              .stream()
                              .map(this::enrichSingleRow)
                              .toList();
    }

    private DataRow enrichSingleRow(final DataRow rowToEnrich)
    {
        final Object[] newValues = new String[this.postProcessedDataHeader.length()];
        System.arraycopy(rowToEnrich.row(), 0, newValues, 0, rowToEnrich.length());

        for (int i = 0, offset = rowToEnrich.row().length; i < this.dataPostProcessors.size(); i++)
        {
            final RegisteredDataPostProcessor dataPostProcessor = this.dataPostProcessors.get(i);

            final int outputColumns = dataPostProcessor.postProcessedDataHeader.length();
            dataPostProcessor.dataPostProcessor.enrichRow(rowToEnrich, new DataRowViewImpl(newValues, offset, outputColumns));
        }

        return new DataRow(this.postProcessedDataHeader, newValues);
    }

    @Override
    public void close() throws IOException
    {
        this.dataSource.close();
    }

    private record RegisteredDataPostProcessor(DataPostProcessor dataPostProcessor, DataHeader postProcessedDataHeader) {}
}
