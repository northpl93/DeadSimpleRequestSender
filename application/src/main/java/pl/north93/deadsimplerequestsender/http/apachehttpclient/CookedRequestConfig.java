package pl.north93.deadsimplerequestsender.http.apachehttpclient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.Method;

import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.http.BodyFactory;
import pl.north93.deadsimplerequestsender.template.Template;

record CookedRequestConfig(
        Template url,
        Method verb,
        Map<String, Template> headers,
        BodyFactory bodyFactory
)
{
    public URI renderUri(final DataRow dataRow)
    {
        return URI.create(this.url.render(dataRow));
    }

    public Map<String, String> renderHeadersForRequest(final DataRow dataRow)
    {
        final Map<String, String> renderedHeaders = new HashMap<>();
        for (final Map.Entry<String, Template> entry : this.headers.entrySet())
        {
            final String renderedHeaderValue = entry.getValue().render(dataRow);
            renderedHeaders.put(entry.getKey(), renderedHeaderValue);
        }

        return renderedHeaders;
    }

    public String createBody(final DataRow dataRow)
    {
        return this.bodyFactory.createBody(dataRow);
    }
}
