package pl.north93.deadsimplerequestsender.job;

import java.io.File;
import java.util.UUID;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceFactory;
import pl.north93.deadsimplerequestsender.environment.ApplicationEnvironment;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSenderConfig;
import pl.north93.deadsimplerequestsender.http.RequestSenderFactory;
import pl.north93.deadsimplerequestsender.http.retry.RetryRequestSender;
import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountCommand;
import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class SubmitJobHandler implements CommandHandler<SubmitJobCommand, Job>
{
    private static final Logger log = LoggerFactory.getLogger(SubmitJobHandler.class);
    private final ApplicationEnvironment applicationEnvironment;
    private final RequestSenderFactory requestSenderFactory;
    private final DataSourceFactory dataSourceFactory;
    private final MessagePublisher messagePublisher;
    private final JobManagement jobManagement;

    @Inject
    public SubmitJobHandler(
            final ApplicationEnvironment applicationEnvironment,
            final RequestSenderFactory requestSenderFactory,
            final DataSourceFactory dataSourceFactory,
            final MessagePublisher messagePublisher,
            final JobManagement jobManagement
    )
    {
        this.applicationEnvironment = applicationEnvironment;
        this.requestSenderFactory = requestSenderFactory;
        this.dataSourceFactory = dataSourceFactory;
        this.messagePublisher = messagePublisher;
        this.jobManagement = jobManagement;
    }

    @Override
    public Job handleCommand(final SubmitJobCommand command)
    {
        final UUID jobId = UUID.randomUUID();
        final JobConfig jobConfig = command.jobConfig();
        log.info("Submitting new job with ID {} and the following configuration {}", jobId, command.jobConfig());

        final File jobLocalWorkDir = this.createJobLocalWorkDir(jobId);
        final DataSource dataSource = this.dataSourceFactory.createDataSource(jobLocalWorkDir, jobConfig.data(), jobConfig.postProcessors());
        final RequestSender requestSender = this.createRequestSender(dataSource, jobConfig);

        final RunningJob runningJob = new RunningJob(jobId, jobLocalWorkDir, jobConfig, dataSource, requestSender, jobConfig.executor().threads());
        this.jobManagement.registerJob(runningJob);

        log.info("Created new job with ID {}", jobId);
        this.messagePublisher.executeCommand(new ChangeWorkerThreadCountCommand(jobId, runningJob.getTargetWorkerThreads()));

        return runningJob;
    }

    private File createJobLocalWorkDir(final UUID jobId)
    {
        final File jobDirectory = new File(new File(this.applicationEnvironment.workingDirectory(), "jobs"), jobId.toString());
        if (jobDirectory.mkdirs())
        {
            log.info("Created job working directory: {}", jobDirectory);
            return jobDirectory;
        }

        throw new IllegalStateException("Failed to create job directory:" + jobDirectory);
    }

    private RequestSender createRequestSender(final DataSource dataSource, final JobConfig jobConfig)
    {
        final int maxConnections = jobConfig.executor().threads() * 2;
        final RequestSenderConfig requestSenderConfig = new RequestSenderConfig(maxConnections, jobConfig.request());

        return new RetryRequestSender(this.requestSenderFactory.createRequestSender(dataSource.readHeader(), requestSenderConfig));
    }
}
