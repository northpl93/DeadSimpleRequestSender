package pl.north93.deadsimplerequestsender.application;

import static pl.north93.deadsimplerequestsender.threading.ThreadingHelper.ensureManagementThread;


import java.io.File;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.application.command.LaunchApplicationCommand;
import pl.north93.deadsimplerequestsender.environment.StartupMode;
import pl.north93.deadsimplerequestsender.environment.StartupMode.DaemonMode;
import pl.north93.deadsimplerequestsender.environment.StartupMode.SingleShotMode;
import pl.north93.deadsimplerequestsender.environment.event.ApplicationStartupEvent;
import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.job.JobDefinitionParsingException;
import pl.north93.deadsimplerequestsender.job.YamlJobConfigLoader;
import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.job.event.ApplicationStandbyEvent;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class LaunchApplicationHandler implements CommandHandler<LaunchApplicationCommand, Void>
{
    private static final Logger log = LoggerFactory.getLogger(LaunchApplicationHandler.class);

    private final YamlJobConfigLoader yamlJobConfigLoader;
    private final MessagePublisher messagePublisher;
    private final StartupMode startupMode;

    @Inject
    public LaunchApplicationHandler(
            final YamlJobConfigLoader yamlJobConfigLoader,
            final MessagePublisher messagePublisher,
            final StartupMode startupMode
    )
    {
        this.yamlJobConfigLoader = yamlJobConfigLoader;
        this.messagePublisher = messagePublisher;
        this.startupMode = startupMode;
    }

    @Override
    public Void handleCommand(final LaunchApplicationCommand command)
    {
        ensureManagementThread();
        log.info("Early initialization complete, launching the DeadSimple*RequestSender");

        this.messagePublisher.publishEvent(new ApplicationStartupEvent());
        if (this.startupMode instanceof final SingleShotMode singleShotMode)
        {
            this.startSingleShotMode(singleShotMode);
        }
        else if (this.startupMode instanceof DaemonMode)
        {
            log.info("Starting in a daemon mode");
        }

        return null;
    }

    private void startSingleShotMode(final SingleShotMode singleShotMode)
    {
        log.info("Starting single-shot mode, the app will shutdown after the job completes");
        try
        {
            final JobConfig jobConfig = this.yamlJobConfigLoader.loadJobConfigFromFile(new File(singleShotMode.jobFile()));
            this.messagePublisher.executeCommand(new SubmitJobCommand(jobConfig));
        }
        catch (final JobDefinitionParsingException e)
        {
            log.error("Failed to parse job definition", e);
            this.messagePublisher.publishEvent(new ApplicationStandbyEvent());
        }
    }
}
