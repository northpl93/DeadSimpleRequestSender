package pl.north93.deadsimplerequestsender.environment;

import java.io.File;

public record ApplicationEnvironment(
        File workingDirectory,
        StartupMode startupMode
)
{
}
