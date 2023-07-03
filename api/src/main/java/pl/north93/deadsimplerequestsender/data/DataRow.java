package pl.north93.deadsimplerequestsender.data;

public record DataRow(DataHeader header, Object[] row)
{
    public Object getValue(final int index)
    {
        return this.row[index];
    }

    public int length()
    {
        return this.row.length;
    }
}
