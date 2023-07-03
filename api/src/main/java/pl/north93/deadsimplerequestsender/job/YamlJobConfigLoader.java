package pl.north93.deadsimplerequestsender.job;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YamlJobConfigLoader
{
    private static final Logger log = LoggerFactory.getLogger(YamlJobConfigLoader.class);
    private final ObjectMapper yamlObjectMapper;

    public YamlJobConfigLoader(final ObjectMapper yamlObjectMapper)
    {
        this.yamlObjectMapper = yamlObjectMapper;
    }

    public JobConfig loadJobConfigFromFile(final File jobDefinition)
    {
        try
        {
            log.info("Loading job definition from file {}", jobDefinition);
            return this.yamlObjectMapper.readValue(jobDefinition, JobConfig.class);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Failed to load job definition", e);
        }
    }
}
