package pl.north93.deadsimplerequestsender.job;

public interface WorkerThread
{
    int getThreadId();

    String getName();

    boolean isTerminationRequested();
}
