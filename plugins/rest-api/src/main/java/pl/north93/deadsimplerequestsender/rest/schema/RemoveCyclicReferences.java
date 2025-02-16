package pl.north93.deadsimplerequestsender.rest.schema;

import java.util.List;

import com.fasterxml.classmate.ResolvedType;
import com.github.victools.jsonschema.generator.ConfigFunction;
import com.github.victools.jsonschema.generator.FieldScope;

// JSON Forms causes stack overflows when it encounters a type declaration with cyclic reference.
final class RemoveCyclicReferences implements ConfigFunction<FieldScope, List<ResolvedType>>
{
    private final SubTypesResolver subTypesResolver;

    public RemoveCyclicReferences(final SubTypesResolver subTypesResolver)
    {
        this.subTypesResolver = subTypesResolver;
    }

    @Override
    public List<ResolvedType> apply(final FieldScope fieldScope)
    {
        // library iterates over all types representable with the given fields and gives them here
        // for example for a field of interface type with two implementations we will observe here these two types
        final ResolvedType fieldScopeType = fieldScope.getType();

        // type of class that declares the field
        final ResolvedType fieldDeclaringClassType = fieldScope.getMember().getDeclaringType();

        if (fieldDeclaringClassType.isInstanceOf(fieldScopeType.getErasedType()))
        {
            return this.subTypesResolver.getSubTypes(fieldScope.getContext(), fieldScope.getMember().getType().getErasedType())
                       .stream()
                       .filter(it -> it != fieldDeclaringClassType)
                       .toList();
        }

        return null;
    }
}
