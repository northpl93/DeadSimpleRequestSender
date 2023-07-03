package pl.north93.deadsimplerequestsender.utils;

import java.io.StringWriter;
import java.util.Locale;

import org.stringtemplate.v4.NoIndentWriter;
import org.stringtemplate.v4.ST;

public final class STUtils
{
    public static String renderWithoutIndent(final ST stringTemplate)
    {
        final StringWriter out = new StringWriter();
        stringTemplate.write(new NoIndentWriter(out), Locale.ENGLISH);
        return out.toString();
    }
}
