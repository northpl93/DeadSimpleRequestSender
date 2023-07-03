package pl.north93.deadsimplerequestsender.data;

public interface DataPostProcessor
{
    DataHeader readHeader();

    void enrichRow(DataRow sourceRow, DataRowView outputRow);
}
