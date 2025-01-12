package pl.north93.deadsimplerequestsender.application;

import static pl.north93.deadsimplerequestsender.threading.ThreadingHelper.ensureManagementThread;


import java.io.File;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.application.command.LaunchApplicationCommand;
import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.environment.StartupMode;
import pl.north93.deadsimplerequestsender.environment.StartupMode.SingleShotMode;
import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.job.YamlJobConfigLoader;
import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class ApplicationLauncher implements CommandHandler<LaunchApplicationCommand, Void>
{
    private static final Logger log = LoggerFactory.getLogger(ApplicationLauncher.class);

    private final ApplicationEnvironment applicationEnvironment;
    private final YamlJobConfigLoader yamlJobConfigLoader;
    private final MessagePublisher messagePublisher;

    @Inject
    public ApplicationLauncher(
            final ApplicationEnvironment applicationEnvironment,
            final YamlJobConfigLoader yamlJobConfigLoader,
            final MessagePublisher messagePublisher
    )
    {
        this.applicationEnvironment = applicationEnvironment;
        this.yamlJobConfigLoader = yamlJobConfigLoader;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public Void handleCommand(final LaunchApplicationCommand command)
    {
        ensureManagementThread();
        log.info("Early initialization complete, launching the DeadSimple*RequestSender");

        if (this.applicationEnvironment.startupMode() instanceof final SingleShotMode singleShotMode)
        {
            final JobConfig jobConfig = this.yamlJobConfigLoader.loadJobConfigFromFile(new File(singleShotMode.jobFile()));
            log.info("{}", jobConfig);

            this.messagePublisher.executeCommand(new SubmitJobCommand(jobConfig));
        }
        else if (this.applicationEnvironment.startupMode() instanceof StartupMode.DaemonMode)
        {
            log.info("Starting in a daemon mode");
        }

        return null;
    }
}
