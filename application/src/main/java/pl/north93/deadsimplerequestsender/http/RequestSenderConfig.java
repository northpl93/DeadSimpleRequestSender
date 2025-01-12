package pl.north93.deadsimplerequestsender.http;

public record RequestSenderConfig(
        int maxConnections,
        RequestConfig requestConfig
)
{
}
