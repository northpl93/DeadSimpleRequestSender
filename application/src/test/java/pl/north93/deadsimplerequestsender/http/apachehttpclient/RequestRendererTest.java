package pl.north93.deadsimplerequestsender.http.apachehttpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;


import static pl.north93.deadsimplerequestsender.http.HttpVerb.GET;
import static pl.north93.deadsimplerequestsender.http.HttpVerb.POST;


import java.net.URI;
import java.util.Map;

import com.google.inject.Guice;

import org.apache.hc.core5.http.Method;
import org.junit.jupiter.api.Test;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.http.BodyFactoryConfig;
import pl.north93.deadsimplerequestsender.http.RequestConfig;
import pl.north93.deadsimplerequestsender.http.TestBodyFactoryConfig;
import pl.north93.deadsimplerequestsender.template.StModule;

public class RequestRendererTest
{
    @Test
    public void shouldRenderUri()
    {
        // GIVEN
        final RequestConfig requestConfig = new RequestConfig("http://example.com/<column1>", GET, Map.of(), bodyFactoryConfig());

        // WHEN
        final CookedRequestConfig cookedRequestConfig = this.requestConfigCooker().cookRequestConfig(requestConfig, exampleDataHeader());
        final URI renderUri = cookedRequestConfig.renderUri(exampleDataRow());

        // THEN
        assertEquals("http://example.com/value1", renderUri.toString());
    }

    @Test
    public void shouldMapHttpVerb()
    {
        // GIVEN
        final RequestConfig requestConfig = new RequestConfig("", POST, Map.of(), bodyFactoryConfig());

        // WHEN
        final CookedRequestConfig cookedRequestConfig = this.requestConfigCooker().cookRequestConfig(requestConfig, exampleDataHeader());

        // THEN
        assertEquals(Method.POST, cookedRequestConfig.verb());
    }

    @Test
    public void shouldParseHttpHeaders()
    {
        // GIVEN
        final Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Content-Language", "<column1>"
        );
        final RequestConfig requestConfig = new RequestConfig("", GET, headers, bodyFactoryConfig());

        // WHEN
        final CookedRequestConfig cookedRequestConfig = this.requestConfigCooker().cookRequestConfig(requestConfig, exampleDataHeader());

        // THEN
        final Map<String, String> expectedHeaders = Map.of(
                "Content-Type", "application/json",
                "Content-Language", "value1"
        );
        assertEquals(expectedHeaders, cookedRequestConfig.renderHeadersForRequest(exampleDataRow()));
    }

    private RequestConfigCooker requestConfigCooker()
    {
        return new RequestConfigCooker(Guice.createInjector(), new StModule().createStTemplateEngine());
    }

    private static BodyFactoryConfig bodyFactoryConfig()
    {
        return new TestBodyFactoryConfig("");
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