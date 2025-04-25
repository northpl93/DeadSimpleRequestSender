package pl.north93.deadsimplerequestsender.credentials;

import java.time.Instant;

public sealed interface Credentials
{
    public record TokenCredentials(String token, Instant expiration) implements Credentials {}
}
