package pl.north93.deadsimplerequestsender.credentials;

import com.google.inject.AbstractModule;

public final class CredentialsModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        this.bind(CredentialsRegistry.class).toProvider(CredentialsRegistryProvider.class).asEagerSingleton();
    }
}
