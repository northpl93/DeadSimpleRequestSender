package pl.north93.deadsimplerequestsender.threading;

import static pl.north93.deadsimplerequestsender.threading.ManagementThread.managementThreadFactory;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class ThreadingModule extends AbstractModule
{
    @Provides
    ExecutorService createManagementExecutor()
    {
        return Executors.newSingleThreadExecutor(managementThreadFactory());
    }
}
