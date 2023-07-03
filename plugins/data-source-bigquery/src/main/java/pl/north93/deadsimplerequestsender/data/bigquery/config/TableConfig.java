package pl.north93.deadsimplerequestsender.data.bigquery.config;

import java.text.MessageFormat;

public record TableConfig(String project, String dataset, String name)
{
    public String formatResourceName()
    {
        return MessageFormat.format("projects/{0}/datasets/{1}/tables/{2}", this.project, this.dataset, this.name);
    }
}
