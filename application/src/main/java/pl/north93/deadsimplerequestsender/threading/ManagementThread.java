package pl.north93.deadsimplerequestsender.threading;

import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ManagementThread extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(ManagementThread.class);

    private ManagementThread(final Runnable runnable)
    {
        super(runnable, "DSRS-Management");
    }

    static ThreadFactory managementThreadFactory()
    {
        return runnable ->
        {
            log.debug("Spawning new DSRS management thread");
            return new ManagementThread(runnable);
        };
    }
}
