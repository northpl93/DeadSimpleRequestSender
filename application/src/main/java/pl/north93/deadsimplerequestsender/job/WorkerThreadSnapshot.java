package pl.north93.deadsimplerequestsender.job;

record WorkerThreadSnapshot(
        int threadId,
        String name,
        boolean terminationRequested
) implements WorkerThread
{
    @Override
    public int getThreadId()
    {
        return this.threadId;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean isTerminationRequested()
    {
        return this.terminationRequested;
    }

    static WorkerThreadSnapshot of(final WorkerThreadImpl workerThread)
    {
        return new WorkerThreadSnapshot(
                workerThread.getThreadId(),
                workerThread.getName(),
                workerThread.isTerminationRequested()
        );
    }
}
