package pl.north93.deadsimplerequestsender.template;

import org.stringtemplate.v4.ST;

import pl.north93.deadsimplerequestsender.data.DataHeader;

final class StTemplateEngine implements TemplateEngine
{
    @Override
    public Template prepareTemplate(final DataHeader dataHeader, final String template)
    {
        return new StTemplate(dataHeader, new ST(template));
    }
}
