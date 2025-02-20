package pl.north93.deadsimplerequestsender.job;

import java.io.File;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class YamlJobConfigLoader
{
    private static final Logger log = LoggerFactory.getLogger(YamlJobConfigLoader.class);
    private final ObjectMapper yamlObjectMapper;

    @Inject
    public YamlJobConfigLoader(final @YamlObjectMapper ObjectMapper yamlObjectMapper)
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
        catch (final Exception e)
        {
            throw new JobDefinitionParsingException(e);
        }
    }

    public String jobConfigToYaml(final JobConfig jobConfig)
    {
        try
        {
            return this.yamlObjectMapper.writeValueAsString(jobConfig);
        }
        catch (final JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
