package pl.north93.deadsimplerequestsender.http;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

public class RequestRendererTest
{
    @Test
    public void shouldRenderUri()
    {
        // GIVEN
        final DataHeader dataHeader = exampleDataHeader();
        final RequestConfig requestConfig = requestConfigWithUrl("http://example.com/<column1>");
        final RequestRenderer requestRenderer = new RequestRenderer(dataHeader, requestConfig);

        // WHEN
        final URI renderUri = requestRenderer.renderUri(exampleDataRow());

        // THEN
        assertEquals("http://example.com/value1", renderUri.toString());
    }

    private static RequestConfig requestConfigWithUrl(final String url)
    {
        return new RequestConfig(url, "GET", Map.of(), new TestBodyFactoryConfig(""));
    }

    private static DataHeader exampleDataHeader()
    {
        return new DataHeader(new String[] {"column1"});
    }

    private static DataRow exampleDataRow()
    {
        return new DataRow(exampleDataHeader(), new String[] {"value1"});
    }
}