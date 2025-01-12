package pl.north93.deadsimplerequestsender.job;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class JobModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        this.bind(YamlJobConfigLoader.class);
        this.bind(JobManagement.class).asEagerSingleton();
        this.bind(SubmitJobHandler.class).asEagerSingleton();
        this.bind(WorkerThreadManagement.class).asEagerSingleton();
        this.bind(ChangeWorkerThreadCountHandler.class).asEagerSingleton();
    }

    @Provides
    public ObjectMapper createYamlObjectMapper(final Set<ObjectMapperCustomizer> customizers)
    {
        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        customizers.forEach(customizer -> customizer.customizeObjectMapper(objectMapper));

        return objectMapper;
    }
}
