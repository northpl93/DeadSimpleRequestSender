package pl.north93.deadsimplerequestsender.http.inline;

import com.fasterxml.jackson.annotation.JsonTypeName;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.http.BodyFactory;
import pl.north93.deadsimplerequestsender.http.BodyFactoryConfig;

@JsonTypeName("inline")
public record InlineBodyFactoryConfig(String template) implements BodyFactoryConfig
{
    @Override
    public BodyFactory createBodyFactory(final DataHeader dataHeader)
    {
        return new InlineBodyFactory(dataHeader, this.template);
    }
}
