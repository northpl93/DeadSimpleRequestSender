package pl.north93.deadsimplerequestsender.job;

import java.util.UUID;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.data.DataSource;
import pl.north93.deadsimplerequestsender.data.DataSourceFactory;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSenderConfig;
import pl.north93.deadsimplerequestsender.http.RequestSenderFactory;
import pl.north93.deadsimplerequestsender.http.retry.RetryRequestSender;
import pl.north93.deadsimplerequestsender.job.command.ChangeWorkerThreadCountCommand;
import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.messaging.CommandHandler;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class SubmitJobHandler implements CommandHandler<SubmitJobCommand, UUID>
{
    private static final Logger log = LoggerFactory.getLogger(SubmitJobHandler.class);
    private final RequestSenderFactory requestSenderFactory;
    private final DataSourceFactory dataSourceFactory;
    private final MessagePublisher messagePublisher;
    private final JobManagement jobManagement;

    @Inject
    public SubmitJobHandler(
            final RequestSenderFactory requestSenderFactory,
            final DataSourceFactory dataSourceFactory,
            final MessagePublisher messagePublisher,
            final JobManagement jobManagement
    )
    {
        this.requestSenderFactory = requestSenderFactory;
        this.dataSourceFactory = dataSourceFactory;
        this.messagePublisher = messagePublisher;
        this.jobManagement = jobManagement;
    }

    @Override
    public UUID handleCommand(final SubmitJobCommand command)
    {
        final UUID jobId = UUID.randomUUID();
        final JobConfig jobConfig = command.jobConfig();

        final DataSource dataSource = this.dataSourceFactory.createDataSource(jobConfig.data(), jobConfig.postProcessors());
        final RequestSender requestSender = this.createRequestSender(dataSource, jobConfig);

        final RunningJob runningJob = new RunningJob(jobId, dataSource, requestSender, jobConfig.executor().threads());
        this.jobManagement.registerJob(runningJob);

        log.info("Created new job with ID {}", jobId);
        this.messagePublisher.executeCommand(new ChangeWorkerThreadCountCommand(jobId, runningJob.getTargetWorkerThreads()));

        return jobId;
    }

    private RequestSender createRequestSender(final DataSource dataSource, final JobConfig jobConfig)
    {
        final int maxConnections = jobConfig.executor().threads() * 2;
        final RequestSenderConfig requestSenderConfig = new RequestSenderConfig(maxConnections, jobConfig.request());

        return new RetryRequestSender(this.requestSenderFactory.createRequestSender(dataSource.readHeader(), requestSenderConfig));
    }
}
