package pl.north93.deadsimplerequestsender.rest.schema;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.github.victools.jsonschema.generator.ConfigFunction;
import com.github.victools.jsonschema.generator.TypeScope;

// JSON Forms shows text from "title" property in "oneOf" dropdown lists.
final class JsonClassDescriptionInTitle implements ConfigFunction<TypeScope, String>
{
    @Override
    public String apply(final TypeScope target)
    {
        final JsonClassDescription jsonClassDescription = target.getType().getErasedType().getAnnotation(JsonClassDescription.class);
        if (jsonClassDescription != null)
        {
            return jsonClassDescription.value();
        }

        return null;
    }
}
