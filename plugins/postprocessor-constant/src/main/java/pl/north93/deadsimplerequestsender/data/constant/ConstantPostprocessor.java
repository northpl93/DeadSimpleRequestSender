package pl.north93.deadsimplerequestsender.data.constant;

import java.util.Map;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataPostProcessor;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.data.DataRowView;

public class ConstantPostprocessor implements DataPostProcessor
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final DataHeader dataHeader;
    private final String[] row;

    public ConstantPostprocessor(final Map<String, String> constantValues)
    {
        this.dataHeader = new DataHeader(constantValues.keySet().toArray(EMPTY_STRING_ARRAY));
        this.row = constantValues.values().toArray(EMPTY_STRING_ARRAY);
    }

    @Override
    public DataHeader readHeader()
    {
        return this.dataHeader;
    }

    @Override
    public void enrichRow(final DataRow sourceRow, final DataRowView outputRow)
    {
        for (int i = 0; i < this.row.length; i++)
        {
            outputRow.setValue(i, this.row[i]);
        }
    }
}
