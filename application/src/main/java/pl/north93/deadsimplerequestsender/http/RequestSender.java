package pl.north93.deadsimplerequestsender.http;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
    private final Method method;
    private final BodyFactory bodyFactory;
    private final RequestRenderer requestRenderer;

    public RequestSender(final DataHeader dataHeader, final RequestConfig requestConfig)
    {
        this.method = Method.normalizedValueOf(requestConfig.verb());
        this.bodyFactory = requestConfig.body().createBodyFactory(dataHeader);
        this.requestRenderer = new RequestRenderer(dataHeader, requestConfig);
    }

    public void sendRequest(final HttpClient httpClient, final DataRow dataRow)
    {
        final URI renderUri = this.requestRenderer.renderUri(dataRow);
        final BasicClassicHttpRequest basicHttpRequest = new BasicClassicHttpRequest(this.method, renderUri);

        final Map<String, String> renderedHeaders = this.requestRenderer.renderHeadersForRequest(dataRow);
        renderedHeaders.forEach(basicHttpRequest::addHeader);

        final String body = this.bodyFactory.createBody(dataRow);
        basicHttpRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));

        try
        {
            httpClient.execute(basicHttpRequest, new BasicHttpClientResponseHandler());
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
