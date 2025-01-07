package pl.north93.deadsimplerequestsender.http.inline;

import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.http.BodyFactory;
import pl.north93.deadsimplerequestsender.template.Template;

public class InlineBodyFactory implements BodyFactory
{
    private final Template bodyTemplate;

    public InlineBodyFactory(final Template template)
    {
        this.bodyTemplate = template;
    }

    @Override
    public String createBody(final DataRow dataRow)
    {
        return this.bodyTemplate.render(dataRow);
    }
}
