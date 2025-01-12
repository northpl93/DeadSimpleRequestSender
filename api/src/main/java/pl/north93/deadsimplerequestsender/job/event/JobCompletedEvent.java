package pl.north93.deadsimplerequestsender.job.event;

import java.util.UUID;

import pl.north93.deadsimplerequestsender.messaging.Event;

public record JobCompletedEvent(UUID jobId) implements Event
{
}
