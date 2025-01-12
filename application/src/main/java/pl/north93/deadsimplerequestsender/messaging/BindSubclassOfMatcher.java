package pl.north93.deadsimplerequestsender.messaging;

import com.google.inject.Binding;
import com.google.inject.matcher.Matcher;

final class BindSubclassOfMatcher implements Matcher<Binding<?>>
{
    private final Class<?> superClass;

    public BindSubclassOfMatcher(final Class<?> superClass)
    {
        this.superClass = superClass;
    }

    @Override
    public boolean matches(final Binding<?> binding)
    {
        return this.superClass.isAssignableFrom(binding.getKey().getTypeLiteral().getRawType());
    }
}
