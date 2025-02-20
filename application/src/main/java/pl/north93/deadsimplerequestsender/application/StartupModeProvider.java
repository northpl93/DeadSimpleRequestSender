package pl.north93.deadsimplerequestsender.application;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import pl.north93.deadsimplerequestsender.environment.OptionsCustomizer;
import pl.north93.deadsimplerequestsender.environment.StartupMode;

final class StartupModeProvider implements Provider<StartupMode>
{
    private static final Option DAEMON_OPTION = new Option("daemon", "start in a daemon mode");
    private final CommandLine commandLine;

    @Inject
    StartupModeProvider(final CommandLine commandLine)
    {
        this.commandLine = commandLine;
    }

    @Override
    public StartupMode get()
    {
        if (this.commandLine.hasOption(DAEMON_OPTION))
        {
            return new StartupMode.DaemonMode();
        }
        else
        {
            final String[] args = this.commandLine.getArgs();
            if (args.length == 0)
            {
                throw new RuntimeException("Please provide path to job definition or launch application in daemon mode");
            }

            return new StartupMode.SingleShotMode(args[0]);
        }
    }

    static final class StartupModeOptionsCustomizer implements OptionsCustomizer
    {
        @Override
        public void customizeOptions(final Options options)
        {
            options.addOption(DAEMON_OPTION);
        }
    }
}
