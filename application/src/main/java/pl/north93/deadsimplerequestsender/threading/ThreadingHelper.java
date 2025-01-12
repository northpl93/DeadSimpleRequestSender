package pl.north93.deadsimplerequestsender.threading;

public final class ThreadingHelper
{
    public static void ensureManagementThread()
    {
        if (Thread.currentThread() instanceof ManagementThread)
        {
            return;
        }

        throw new WrongThreadException("This method can be called only from management thread");
    }
}
