package pl.north93.deadsimplerequestsender.job;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import pl.north93.deadsimplerequestsender.http.RequestSenderFactory;

public class JobModule extends AbstractModule
{
    @Provides
    public JobManagement createJobManagement(final RequestSenderFactory requestSenderFactory)
    {
        return new JobManagement(requestSenderFactory);
    }

    @Provides
    public ObjectMapper createYamlObjectMapper(final Set<ObjectMapperCustomizer> customizers)
    {
        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        customizers.forEach(customizer -> customizer.customizeObjectMapper(objectMapper));

        return objectMapper;
    }

    @Provides
    public YamlJobConfigLoader createYamlJobConfigLoader(final ObjectMapper yamlObjectMapper)
    {
        return new YamlJobConfigLoader(yamlObjectMapper);
    }
}
