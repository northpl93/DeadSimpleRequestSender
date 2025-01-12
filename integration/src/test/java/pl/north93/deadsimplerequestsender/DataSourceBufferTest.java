package pl.north93.deadsimplerequestsender;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.matching.RequestPatternBuilder.allRequests;


import java.util.UUID;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import pl.north93.deadsimplerequestsender.integration.DeadSimpleRequestSenderExtension;
import pl.north93.deadsimplerequestsender.integration.IntegrationTestJobHelper;

@WireMockTest(httpPort = 2999)
@ExtendWith(DeadSimpleRequestSenderExtension.class)
public class DataSourceBufferTest
{
    @Test
    public void shouldProperlyBufferData(final IntegrationTestJobHelper integrationTestJobHelper)
    {
        // GIVEN
        stubFor(get("/test").willReturn(ok()));

        // WHEN
        final UUID jobId = integrationTestJobHelper.submitJob("src/test/resources/buffer/BufferWithRandom10000.yaml");
        integrationTestJobHelper.awaitJobCompletion(jobId);

        // THEN
        final RequestPatternBuilder requestPattern =
                allRequests()
                        .withUrl("/test")
                        .withHeader("Content-Type", equalTo("application/json"));
        verify(10000, requestPattern);
    }

    @Test
    public void shouldProperlyBufferDataWhenThereAreMoreWorkersThanRows(final IntegrationTestJobHelper integrationTestJobHelper)
    {
        // GIVEN
        stubFor(get("/test").willReturn(ok()));

        // WHEN
        final UUID jobId = integrationTestJobHelper.submitJob("src/test/resources/buffer/BufferWithRandom4.yaml");
        integrationTestJobHelper.awaitJobCompletion(jobId);

        // THEN
        final RequestPatternBuilder requestPattern =
                allRequests()
                        .withUrl("/test")
                        .withHeader("Content-Type", equalTo("application/json"));
        verify(4, requestPattern);
    }
}
