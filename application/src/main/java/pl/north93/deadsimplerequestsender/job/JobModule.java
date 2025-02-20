package pl.north93.deadsimplerequestsender.job;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.USE_NATIVE_TYPE_ID;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;


import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

public final class JobModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        Multibinder.newSetBinder(this.binder(), ObjectMapperCustomizer.class);
        this.bind(YamlJobConfigLoader.class);
        this.bind(JobManagement.class).asEagerSingleton();
        this.bind(SubmitJobHandler.class).asEagerSingleton();
        this.bind(WorkerThreadManagement.class).asEagerSingleton();
        this.bind(ChangeWorkerThreadCountHandler.class).asEagerSingleton();
        this.bind(GetJobSnapshotHandler.class).asEagerSingleton();
        this.bind(GetJobListSnapshotHandler.class).asEagerSingleton();
        this.bind(TerminateJobHandler.class).asEagerSingleton();
        this.bind(GetJobWorkerThreadsSnapshotHandler.class).asEagerSingleton();
        this.bind(JobFacade.class).to(JobFacadeImpl.class);
    }

    @Provides
    @YamlObjectMapper
    public ObjectMapper createYamlObjectMapper(final Set<ObjectMapperCustomizer> customizers)
    {
        final YAMLFactory yamlFactory =
                new YAMLFactory()
                        .disable(WRITE_DOC_START_MARKER)
                        .disable(USE_NATIVE_TYPE_ID)
                        .enable(INDENT_ARRAYS_WITH_INDICATOR);

        final ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        customizers.forEach(customizer -> customizer.customizeObjectMapper(objectMapper));

        return objectMapper;
    }

    @Provides
    @JsonObjectMapper
    public ObjectMapper createJsonObjectMapper(final Set<ObjectMapperCustomizer> customizers)
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        customizers.forEach(customizer -> customizer.customizeObjectMapper(objectMapper));

        return objectMapper;
    }
}
