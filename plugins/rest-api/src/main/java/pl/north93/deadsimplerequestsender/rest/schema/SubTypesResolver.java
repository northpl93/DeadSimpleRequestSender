package pl.north93.deadsimplerequestsender.rest.schema;

import java.util.Collection;
import java.util.List;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.SubtypeResolver;
import com.github.victools.jsonschema.generator.TypeContext;

// Automatically extract list of concrete implementations registered in ObjectMapper.
final class SubTypesResolver implements SubtypeResolver
{
    private final ObjectMapper objectMapper;

    public SubTypesResolver(final ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ResolvedType> findSubtypes(final ResolvedType declaredType, final SchemaGenerationContext schemaGenerationContext)
    {
        if (declaredType.getErasedType().isAnnotationPresent(JsonTypeInfo.class))
        {
            return this.getSubTypes(schemaGenerationContext.getTypeContext(), declaredType.getErasedType());
        }

        return null;
    }

    public List<ResolvedType> getSubTypes(final TypeContext typeContext, final Class<?> ofClass)
    {
        final JavaType javaType = this.objectMapper.constructType(ofClass);
        final Collection<NamedType> namedTypes = this.objectMapper.getSubtypeResolver().collectAndResolveSubtypesByClass(this.objectMapper.getDeserializationConfig(), null, javaType);
        return namedTypes.stream()
                         .filter(it -> it.getType() != ofClass)
                         .map(it -> typeContext.resolve(it.getType()))
                         .toList();
    }
}
