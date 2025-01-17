package pl.north93.deadsimplerequestsender.integration;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.Job;
import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.job.YamlJobConfigLoader;
import pl.north93.deadsimplerequestsender.job.command.SubmitJobCommand;
import pl.north93.deadsimplerequestsender.job.event.JobCompletedEvent;
import pl.north93.deadsimplerequestsender.messaging.EventListener;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

public class IntegrationTestJobHelper implements EventListener
{
    private static final Logger log = LoggerFactory.getLogger(IntegrationTestJobHelper.class);
    private final MessagePublisher messagePublisher;
    private final YamlJobConfigLoader yamlJobConfigLoader;
    private final Map<UUID, CompletableFuture<Void>> locks = new ConcurrentHashMap<>();

    @Inject
    public IntegrationTestJobHelper(final MessagePublisher messagePublisher, final YamlJobConfigLoader yamlJobConfigLoader)
    {
        this.messagePublisher = messagePublisher;
        this.yamlJobConfigLoader = yamlJobConfigLoader;
    }

    public UUID submitJob(final String path)
    {
        final JobConfig jobConfig = this.yamlJobConfigLoader.loadJobConfigFromFile(new File(path));
        final CompletableFuture<Job> dispatchJobFuture = this.messagePublisher.executeCommand(new SubmitJobCommand(jobConfig));

        try
        {
            return dispatchJobFuture.get().getJobId();
        }
        catch (final InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void awaitJobCompletion(final UUID jobId)
    {
        final CompletableFuture<Void> completableFuture = this.locks.computeIfAbsent(jobId, k -> new CompletableFuture<>());
        try
        {
            log.info("Waiting for completion of {}", jobId);
            completableFuture.get();
        }
        catch (final InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            log.info("Job {} completed", jobId);
        }
    }

    @Subscribe
    private void onJobCompleted(final JobCompletedEvent jobCompletedEvent)
    {
        final CompletableFuture<Void> completableFuture = this.locks.computeIfAbsent(jobCompletedEvent.jobId(), k -> new CompletableFuture<>());
        completableFuture.complete(null);
    }
}
