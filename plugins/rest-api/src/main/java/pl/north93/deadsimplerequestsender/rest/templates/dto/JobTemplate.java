package pl.north93.deadsimplerequestsender.rest.templates.dto;

import pl.north93.deadsimplerequestsender.job.JobConfig;

public record JobTemplate(
        String name,
        String description,
        JobConfig definition
)
{
}
