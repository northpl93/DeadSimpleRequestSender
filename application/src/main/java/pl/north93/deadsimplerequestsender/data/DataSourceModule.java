package pl.north93.deadsimplerequestsender.data;

import com.google.inject.AbstractModule;

public final class DataSourceModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        this.bind(DataSourceFactory.class);
    }
}
