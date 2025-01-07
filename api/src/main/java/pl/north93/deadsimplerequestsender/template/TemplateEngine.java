package pl.north93.deadsimplerequestsender.template;

import pl.north93.deadsimplerequestsender.data.DataHeader;

public interface TemplateEngine
{
    Template prepareTemplate(DataHeader dataHeader, String template);
}
