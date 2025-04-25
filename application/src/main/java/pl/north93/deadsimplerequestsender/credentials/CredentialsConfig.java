package pl.north93.deadsimplerequestsender.credentials;

import java.util.List;

record CredentialsConfig(List<CredentialsProviderConfig> credentialProviders)
{
}
