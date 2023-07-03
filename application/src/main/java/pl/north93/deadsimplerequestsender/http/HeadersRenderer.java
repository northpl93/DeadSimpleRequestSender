package pl.north93.deadsimplerequestsender.http;

import static pl.north93.deadsimplerequestsender.utils.STUtils.renderWithoutIndent;


import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.ST;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

public class HeadersRenderer
{
    private final String[] columns;
    private final Map<String, ST> headerTemplates = new HashMap<>();

    public HeadersRenderer(final DataHeader dataHeader, final Map<String, String> headers)
    {
        this.columns = dataHeader.columns();
        for (final Map.Entry<String, String> entry : headers.entrySet())
        {
            this.headerTemplates.put(entry.getKey(), new ST(entry.getValue()));
        }
    }

    public Map<String, String> renderHeadersForRequest(final DataRow dataRow)
    {
        final Map<String, String> renderedHeaders = new HashMap<>();
        for (final Map.Entry<String, ST> entry : this.headerTemplates.entrySet())
        {
            final ST instance = new ST(entry.getValue());
            for (int i = 0; i < this.columns.length; i++)
            {
                instance.add(this.columns[i], dataRow.getValue(i));
            }

            renderedHeaders.put(entry.getKey(), renderWithoutIndent(instance));
        }

        return renderedHeaders;
    }
}
