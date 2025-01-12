package pl.north93.deadsimplerequestsender.environment;

public sealed interface StartupMode
{
    record DaemonMode() implements StartupMode {}

    record SingleShotMode(String jobFile) implements StartupMode {}
}
