package pl.north93.deadsimplerequestsender.rest.templates;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.job.YamlObjectMapper;
import pl.north93.deadsimplerequestsender.rest.templates.dto.JobTemplate;

public final class TemplatesLoader
{
    private static final Logger log = LoggerFactory.getLogger(TemplatesLoader.class);
    private final ApplicationEnvironment applicationEnvironment;
    private final ObjectMapper objectMapper;

    @Inject
    public TemplatesLoader(final ApplicationEnvironment applicationEnvironment, final @YamlObjectMapper ObjectMapper objectMapper)
    {
        this.applicationEnvironment = applicationEnvironment;
        this.objectMapper = objectMapper;
    }

    List<JobTemplate> getTemplates()
    {
        final File templatesDir = new File(this.applicationEnvironment.workingDirectory(), "templates");

        return Optional.ofNullable(templatesDir.listFiles(this.suffixFilter(".yml", ".yaml")))
                       .stream()
                       .flatMap(Arrays::stream)
                       .map(this::parseFile)
                       .flatMap(Optional::stream)
                       .toList();
    }

    private Optional<JobTemplate> parseFile(final File file)
    {
        try
        {
            return Optional.of(this.objectMapper.readValue(file, JobTemplate.class));
        }
        catch (final IOException e)
        {
            log.warn("Failed to parse file {} as job template", file, e);
            return Optional.empty();
        }
    }

    private FileFilter suffixFilter(final String... suffixes)
    {
        return file -> {
            final String fileName = file.getName();
            return Arrays.stream(suffixes).anyMatch(fileName::endsWith);
        };
    }
}
