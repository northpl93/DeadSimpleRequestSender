package pl.north93.deadsimplerequestsender.credentials;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface CredentialsProviderConfig
{
    String uniqueId();

    CredentialsProvider createCredentialsProvider();
}
