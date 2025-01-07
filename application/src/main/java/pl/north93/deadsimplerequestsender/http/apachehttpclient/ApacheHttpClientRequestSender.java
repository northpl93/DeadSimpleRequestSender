package pl.north93.deadsimplerequestsender.http.apachehttpclient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;

import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSendingException;

final class ApacheHttpClientRequestSender implements RequestSender
{
    private final CloseableHttpClient httpClient;
    private final CookedRequestConfig cookedRequestConfig;

    public ApacheHttpClientRequestSender(final CloseableHttpClient httpClient, final CookedRequestConfig cookedRequestConfig)
    {
        this.httpClient = httpClient;
        this.cookedRequestConfig = cookedRequestConfig;
    }

    @Override
    public void sendRequest(final DataRow dataRow) throws RequestSendingException
    {
        final URI renderUri = this.cookedRequestConfig.renderUri(dataRow);
        final BasicClassicHttpRequest basicHttpRequest = new BasicClassicHttpRequest(this.cookedRequestConfig.verb(), renderUri);

        final Map<String, String> renderedHeaders = this.cookedRequestConfig.renderHeadersForRequest(dataRow);
        renderedHeaders.forEach(basicHttpRequest::addHeader);

        final String body = this.cookedRequestConfig.createBody(dataRow);
        basicHttpRequest.setEntity(new StringEntity(body, StandardCharsets.UTF_8));

        try
        {
            this.httpClient.execute(basicHttpRequest, new BasicHttpClientResponseHandler());
        }
        catch (final IOException e)
        {
            throw new RequestSendingException(e);
        }
    }

    @Override
    public void close() throws Exception
    {
        this.httpClient.close();
    }
}
