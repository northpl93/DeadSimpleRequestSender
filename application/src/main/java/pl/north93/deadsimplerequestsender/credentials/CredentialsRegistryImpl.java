package pl.north93.deadsimplerequestsender.credentials;

import java.util.Map;
import java.util.Set;

final class CredentialsRegistryImpl implements CredentialsRegistry
{
    private final Map<String, CredentialsProvider> credentialsProviders;

    CredentialsRegistryImpl(final Map<String, CredentialsProvider> credentialsProviders)
    {
        this.credentialsProviders = credentialsProviders;
    }

    @Override
    public Set<String> getCredentialProviders()
    {
        return this.credentialsProviders.keySet();
    }

    @Override
    public CredentialsProvider getCredentialProviderById(final String id)
    {
        return this.credentialsProviders.get(id);
    }
}
