package pl.north93.deadsimplerequestsender.application;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.environment.OptionsCustomizer;

final class ParsedCommandLineProvider implements Provider<CommandLine>
{
    private final ApplicationEnvironment applicationEnvironment;
    private final Set<OptionsCustomizer> optionsCustomizers;

    @Inject
    public ParsedCommandLineProvider(final ApplicationEnvironment applicationEnvironment, final Set<OptionsCustomizer> optionsCustomizers)
    {
        this.applicationEnvironment = applicationEnvironment;
        this.optionsCustomizers = optionsCustomizers;
    }

    @Override
    public CommandLine get()
    {
        final Options options = new Options();
        this.optionsCustomizers.forEach(optionsCustomizer -> optionsCustomizer.customizeOptions(options));

        final CommandLineParser parser = new DefaultParser();
        try
        {
            return parser.parse(options, this.applicationEnvironment.args());
        }
        catch (final ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
