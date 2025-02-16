package pl.north93.deadsimplerequestsender.rest.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.google.inject.Inject;

import io.javalin.http.Context;
import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.job.YamlJobConfigLoader;
import pl.north93.deadsimplerequestsender.job.YamlObjectMapper;

public final class FormSchemaResource
{
    private final YamlJobConfigLoader yamlJobConfigLoader;
    private final ObjectMapper objectMapper;

    @Inject
    public FormSchemaResource(final YamlJobConfigLoader yamlJobConfigLoader, final @YamlObjectMapper ObjectMapper objectMapper)
    {
        this.yamlJobConfigLoader = yamlJobConfigLoader;
        this.objectMapper = objectMapper;
    }

    public void getSchema(final Context ctx)
    {
        ctx.json(this.generateSchema());
    }

    private JsonNode generateSchema()
    {
        final JacksonModule module = new JacksonModule();
        final SchemaGeneratorConfigBuilder configBuilder =
                new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
                        .without(Option.SCHEMA_VERSION_INDICATOR)
                        .with(Option.MAP_VALUES_AS_ADDITIONAL_PROPERTIES)
                        .with(Option.INLINE_ALL_SCHEMAS)
                        .withObjectMapper(this.objectMapper)
                        .with(module);

        final SubTypesResolver subTypesResolver = new SubTypesResolver(this.objectMapper);
        configBuilder.forTypesInGeneral().withSubtypeResolver(subTypesResolver);

        configBuilder.forFields().withTargetTypeOverridesResolver(new RemoveCyclicReferences(subTypesResolver));

        configBuilder.forTypesInGeneral().withTitleResolver(new JsonClassDescriptionInTitle());
        configBuilder.forTypesInGeneral().withTypeAttributeOverride(new AddDefaultToConstFields());
        configBuilder.forTypesInGeneral().withTypeAttributeOverride(new AddDefaultObjectToMapFields());

        final SchemaGeneratorConfig config = new DelegatingSchemaGeneratorConfig(configBuilder.build());

        final SchemaGenerator generator = new SchemaGenerator(config);
        return generator.generateSchema(JobConfig.class);
    }

    public void generateYamlRepresentation(final Context ctx)
    {
        final JobConfig jobConfig = ctx.bodyAsClass(JobConfig.class);

        ctx.result(this.yamlJobConfigLoader.jobConfigToYaml(jobConfig));
    }
}
