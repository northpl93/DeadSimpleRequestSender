package pl.north93.deadsimplerequestsender.http;

import java.util.Map;

public record RequestConfig(
        String url,
        String verb,
        Map<String, String> headers,
        BodyFactoryConfig body
)
{
}
