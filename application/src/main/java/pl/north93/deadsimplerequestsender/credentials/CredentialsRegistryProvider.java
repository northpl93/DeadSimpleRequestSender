package pl.north93.deadsimplerequestsender.credentials;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.job.YamlObjectMapper;

final class CredentialsRegistryProvider implements Provider<CredentialsRegistry>
{
    private static final Logger log = LoggerFactory.getLogger(CredentialsRegistryProvider.class);
    private final ApplicationEnvironment environment;
    private final ObjectMapper objectMapper;

    @Inject
    public CredentialsRegistryProvider(final ApplicationEnvironment environment, final @YamlObjectMapper ObjectMapper yamlObjectMapper)
    {
        this.environment = environment;
        this.objectMapper = yamlObjectMapper;
    }

    @Override
    public CredentialsRegistry get()
    {
        final Map<String, CredentialsProvider> credentialsProviders = new HashMap<>();

        final CredentialsConfig credentialsConfig = this.loadConfig();
        for (final CredentialsProviderConfig credentialsProviderConfig : credentialsConfig.credentialProviders())
        {
            credentialsProviders.put(credentialsProviderConfig.uniqueId(), credentialsProviderConfig.createCredentialsProvider());
        }

        log.info("Loaded {} credentials providers", credentialsProviders.size());
        return new CredentialsRegistryImpl(credentialsProviders);
    }

    private CredentialsConfig loadConfig()
    {
        final File credentialsFile = new File(this.environment.workingDirectory(), "credentials.yaml");
        if (! credentialsFile.exists())
        {
            return new CredentialsConfig(List.of());
        }

        try
        {
            return objectMapper.readValue(credentialsFile, CredentialsConfig.class);
        }
        catch (final IOException e)
        {
            log.error("Failed to load credentials config", e);
            return new CredentialsConfig(List.of());
        }
    }
}
