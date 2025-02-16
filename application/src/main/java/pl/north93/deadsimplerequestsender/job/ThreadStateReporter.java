package pl.north93.deadsimplerequestsender.job;

import java.util.UUID;

import pl.north93.deadsimplerequestsender.job.event.ThreadExitedEvent;
import pl.north93.deadsimplerequestsender.messaging.MessagePublisher;

final class ThreadStateReporter
{
    private final MessagePublisher messagePublisher;
    private final UUID jobId;

    public ThreadStateReporter(final MessagePublisher messagePublisher, final UUID jobId)
    {
        this.messagePublisher = messagePublisher;
        this.jobId = jobId;
    }

    void reportThreadExit(final WorkerThreadImpl workerThread)
    {
        this.messagePublisher.publishEvent(new ThreadExitedEvent(this.jobId, workerThread.getThreadId()));
    }
}
