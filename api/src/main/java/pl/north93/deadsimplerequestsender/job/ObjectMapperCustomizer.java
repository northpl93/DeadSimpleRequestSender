package pl.north93.deadsimplerequestsender.job;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Customizes ObjectMapper instance used to load YAML job definitions.
 */
public interface ObjectMapperCustomizer
{
    void customizeObjectMapper(ObjectMapper objectMapper);
}
