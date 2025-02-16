package pl.north93.deadsimplerequestsender.rest.schema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.TypeAttributeOverrideV2;
import com.github.victools.jsonschema.generator.TypeScope;

// JSON Forms throws exceptions when it encounters "additionalProperties" without
// some default value.
final class AddDefaultObjectToMapFields implements TypeAttributeOverrideV2
{
    @Override
    public void overrideTypeAttributes(final ObjectNode collectedTypeAttributes, final TypeScope scope, final SchemaGenerationContext context)
    {
        final ObjectNode mapField = collectedTypeAttributes.findParent("additionalProperties");
        if (mapField != null)
        {
            mapField.putObject("default");
        }
    }
}
