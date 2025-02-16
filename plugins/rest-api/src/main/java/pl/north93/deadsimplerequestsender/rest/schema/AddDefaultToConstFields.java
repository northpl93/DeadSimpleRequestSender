package pl.north93.deadsimplerequestsender.rest.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.TypeAttributeOverrideV2;
import com.github.victools.jsonschema.generator.TypeScope;

// JSON Forms doesn't copy constant values from schema to the data.
// We must add the "default" field to generate a JSON containing class identifiers,
// so we can deserialize interfaces.
final class AddDefaultToConstFields implements TypeAttributeOverrideV2
{
    @Override
    public void overrideTypeAttributes(final ObjectNode collectedTypeAttributes, final TypeScope scope, final SchemaGenerationContext context)
    {
        final ObjectNode typeDefinition = collectedTypeAttributes.findParent("const");
        if (typeDefinition != null)
        {
            final String typeName = typeDefinition.get("const").asText();
            typeDefinition.put("default", typeName);
        }
    }
}
