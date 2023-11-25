package pl.north93.deadsimplerequestsender.http;

import static pl.north93.deadsimplerequestsender.utils.STUtils.renderWithoutIndent;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.ST;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

public class RequestRenderer
{
    private final String[] columns;
    private final ST uriTemplate;
    private final Map<String, ST> headerTemplates = new HashMap<>();

    public RequestRenderer(final DataHeader dataHeader, final RequestConfig requestConfig)
    {
        this.columns = dataHeader.columns();
        this.uriTemplate = new ST(requestConfig.url());
        for (final Map.Entry<String, String> entry : requestConfig.headers().entrySet())
        {
            this.headerTemplates.put(entry.getKey(), new ST(entry.getValue()));
        }
    }

    public URI renderUri(final DataRow dataRow)
    {
        return URI.create(this.renderString(this.uriTemplate, dataRow));
    }

    public Map<String, String> renderHeadersForRequest(final DataRow dataRow)
    {
        final Map<String, String> renderedHeaders = new HashMap<>();
        for (final Map.Entry<String, ST> entry : this.headerTemplates.entrySet())
        {
            final String renderedHeaderValue = this.renderString(entry.getValue(), dataRow);
            renderedHeaders.put(entry.getKey(), renderedHeaderValue);
        }

        return renderedHeaders;
    }

    private String renderString(final ST template, final DataRow dataRow)
    {
        final ST instance = new ST(template);
        for (int i = 0; i < this.columns.length; i++)
        {
            instance.add(this.columns[i], dataRow.getValue(i));
        }

        return renderWithoutIndent(instance);
    }
}
