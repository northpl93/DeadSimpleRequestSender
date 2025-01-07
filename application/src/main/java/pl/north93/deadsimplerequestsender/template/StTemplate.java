package pl.north93.deadsimplerequestsender.template;

import java.io.StringWriter;
import java.util.Locale;

import org.stringtemplate.v4.NoIndentWriter;
import org.stringtemplate.v4.ST;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataRow;

final class StTemplate implements Template
{
    private final String[] columns;
    private final ST template;

    public StTemplate(final DataHeader dataHeader, final ST template)
    {
        this.columns = dataHeader.columns();
        this.template = template;
    }

    @Override
    public String render(final DataRow dataRow)
    {
        final ST instance = new ST(this.template);
        for (int i = 0; i < this.columns.length; i++)
        {
            instance.add(this.columns[i], dataRow.getValue(i));
        }

        return renderWithoutIndent(instance);
    }

    private static String renderWithoutIndent(final ST stringTemplate)
    {
        final StringWriter out = new StringWriter();
        stringTemplate.write(new NoIndentWriter(out), Locale.ENGLISH);
        return out.toString();
    }
}
