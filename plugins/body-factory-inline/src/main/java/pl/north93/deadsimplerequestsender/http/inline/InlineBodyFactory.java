package pl.north93.deadsimplerequestsender.http.inline;

import static pl.north93.deadsimplerequestsender.utils.STUtils.renderWithoutIndent;


import org.stringtemplate.v4.ST;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.http.BodyFactory;

public class InlineBodyFactory implements BodyFactory
{
    private final String[] columns;
    private final ST bodyTemplate;

    public InlineBodyFactory(final DataHeader dataHeader, final String template)
    {
        this.columns = dataHeader.columns();
        this.bodyTemplate = new ST(template);
    }

    @Override
    public String createBody(final DataRow dataRow)
    {
        final ST instance = new ST(this.bodyTemplate);
        for (int i = 0; i < this.columns.length; i++)
        {
            instance.add(this.columns[i], dataRow.getValue(i));
        }

        return renderWithoutIndent(instance);
    }
}
