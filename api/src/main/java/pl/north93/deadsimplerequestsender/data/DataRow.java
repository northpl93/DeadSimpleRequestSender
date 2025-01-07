package pl.north93.deadsimplerequestsender.data;

import com.google.common.base.MoreObjects;

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

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("header", this.header)
                          .add("row", this.row)
                          .toString();
    }
}
