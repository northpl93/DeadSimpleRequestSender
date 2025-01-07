package pl.north93.deadsimplerequestsender.http.inline;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.http.BodyFactory;
import pl.north93.deadsimplerequestsender.http.BodyFactoryConfig;
import pl.north93.deadsimplerequestsender.template.Template;
import pl.north93.deadsimplerequestsender.template.TemplateEngine;

@JsonTypeName("inline")
public record InlineBodyFactoryConfig(String template) implements BodyFactoryConfig
{
    @Override
    public BodyFactory createBodyFactory(final Injector injector, final DataHeader dataHeader)
    {
        final TemplateEngine templateEngine = injector.getInstance(TemplateEngine.class);
        final Template bodyTemplate = templateEngine.prepareTemplate(dataHeader, this.template);

        return new InlineBodyFactory(bodyTemplate);
    }
}
