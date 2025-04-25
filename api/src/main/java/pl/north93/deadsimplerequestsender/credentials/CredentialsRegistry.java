package pl.north93.deadsimplerequestsender.credentials;

import java.util.Set;

public interface CredentialsRegistry
{
    Set<String> getCredentialProviders();

    CredentialsProvider getCredentialProviderById(String id);
}
