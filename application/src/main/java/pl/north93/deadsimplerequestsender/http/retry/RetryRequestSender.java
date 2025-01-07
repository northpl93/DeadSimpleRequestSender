package pl.north93.deadsimplerequestsender.http.retry;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.failsafe.Failsafe;
import dev.failsafe.FailsafeException;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;
import dev.failsafe.event.ExecutionAttemptedEvent;
import pl.north93.deadsimplerequestsender.data.DataRow;
import pl.north93.deadsimplerequestsender.http.RequestSender;
import pl.north93.deadsimplerequestsender.http.RequestSendingException;

public class RetryRequestSender implements RequestSender
{
    private static final Logger log = LoggerFactory.getLogger(RetryRequestSender.class);
    private final RequestSender wrappedRequestSender;
    private final FailsafeExecutor<?> failsafe;

    public RetryRequestSender(final RequestSender wrappedRequestSender)
    {
        this.wrappedRequestSender = wrappedRequestSender;

        final RetryPolicy<Object> retryPolicy =
                RetryPolicy.builder()
                           .handle(RequestSendingException.class)
                           .withDelay(Duration.ofSeconds(1))
                           .withMaxRetries(3)
                           .onRetry(this::handleOnRetry)
                           .build();

        this.failsafe = Failsafe.with(retryPolicy);
    }

    @Override
    public void sendRequest(final DataRow dataRow) throws RequestSendingException
    {
        try
        {
            this.failsafe.run(() -> this.wrappedRequestSender.sendRequest(dataRow));
        }
        catch (final FailsafeException e)
        {
            // sendRequest() callers expect to receive an RequestSendingException, so
            // we're trying to unpack the FailsafeException.
            if (e.getCause() instanceof final RequestSendingException requestSendingException)
            {
                throw requestSendingException;
            }

            throw e;
        }
    }

    private void handleOnRetry(final ExecutionAttemptedEvent<?> event)
    {
        log.warn("Retrying request execution because of an exception", event.getLastException());
    }

    @Override
    public void close() throws Exception
    {
        this.wrappedRequestSender.close();
    }
}
