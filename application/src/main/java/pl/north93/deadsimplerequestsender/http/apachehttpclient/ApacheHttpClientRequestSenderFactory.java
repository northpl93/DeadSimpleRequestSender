package pl.north93.deadsimplerequestsender.http.apachehttpclient;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSenderConfig;
import pl.north93.deadsimplerequestsender.http.RequestSenderFactory;

final class ApacheHttpClientRequestSenderFactory implements RequestSenderFactory
{
    private final RequestConfigCooker requestConfigCooker;

    public ApacheHttpClientRequestSenderFactory(final RequestConfigCooker requestConfigCooker)
    {
        this.requestConfigCooker = requestConfigCooker;
    }

    @Override
    public RequestSender createRequestSender(final DataHeader dataHeader, final RequestSenderConfig requestSenderConfig)
    {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(requestSenderConfig.maxConnections());
        connectionManager.setMaxTotal(requestSenderConfig.maxConnections());

        final CloseableHttpClient httpClient =
                HttpClientBuilder.create()
                                 .setConnectionManager(connectionManager)
                                 .build();


        final CookedRequestConfig cookedRequestConfig = this.requestConfigCooker.cookRequestConfig(requestSenderConfig.requestConfig(), dataHeader);

        return new ApacheHttpClientRequestSender(httpClient, cookedRequestConfig);
    }
}
