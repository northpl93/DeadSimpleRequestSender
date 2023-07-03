package pl.north93.deadsimplerequestsender.data;

public record DataHeader(String[] columns)
{
    public int length()
    {
        return this.columns.length;
    }

    public DataHeader append(final DataHeader otherHeader)
    {
        final String[] newColumns = new String[this.columns.length + otherHeader.columns.length];
        System.arraycopy(this.columns, 0, newColumns, 0, this.columns.length);
        System.arraycopy(otherHeader.columns, 0, newColumns, this.columns.length, otherHeader.columns.length);

        return new DataHeader(newColumns);
    }
}