package pl.north93.deadsimplerequestsender.environment;

import org.apache.commons.cli.Options;

@FunctionalInterface
public interface OptionsCustomizer
{
    void customizeOptions(Options options);
}
