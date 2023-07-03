package pl.north93.deadsimplerequestsender.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

public class RequestSender
{
    private final URI uri;
    private final Method method;
    private final BodyFactory bodyFactory;
    private final HeadersRenderer headersRenderer;

    public RequestSender(final DataHeader dataHeader, final RequestConfig requestConfig)
    {
        this.uri = createUri(requestConfig.url());
        this.method = Method.normalizedValueOf(requestConfig.verb());
        this.bodyFactory = requestConfig.body().createBodyFactory(dataHeader);
        this.headersRenderer = new HeadersRenderer(dataHeader, requestConfig.headers());
    }

    public void sendRequest(final HttpClient httpClient, final DataRow dataRow)
    {
        final BasicClassicHttpRequest basicHttpRequest = new BasicClassicHttpRequest(this.method, this.uri);

        final Map<String, String> renderedHeaders = this.headersRenderer.renderHeadersForRequest(dataRow);
        renderedHeaders.forEach(basicHttpRequest::addHeader);

        final String body = this.bodyFactory.createBody(dataRow);
        //System.out.println(body);
        basicHttpRequest.setEntity(new StringEntity(body));

        try
        {
            httpClient.execute(basicHttpRequest, new BasicHttpClientResponseHandler());
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static URI createUri(final String uri)
    {
        try
        {
            return new URI(uri);
        }
        catch (final URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }
}
