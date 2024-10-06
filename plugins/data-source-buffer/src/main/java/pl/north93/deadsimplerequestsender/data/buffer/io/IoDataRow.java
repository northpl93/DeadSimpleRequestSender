package pl.north93.deadsimplerequestsender.data.buffer.io;

import com.google.common.base.Preconditions;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

public record IoDataRow(Object[] row)
{
    public int length()
    {
        return this.row.length;
    }

    public static IoDataRow toIoDataRow(final DataRow dataRow)
    {
        return new IoDataRow(dataRow.row());
    }

    public static DataRow toDomainDataRow(final DataHeader dataHeader, final IoDataRow binDataRow)
    {
        Preconditions.checkArgument(dataHeader.length() == binDataRow.length());
        return new DataRow(dataHeader, binDataRow.row);
    }
}
