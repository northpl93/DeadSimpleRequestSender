package pl.north93.deadsimplerequestsender.http;

import pl.north93.deadsimplerequestsender.data.DataHeader;

public record TestBodyFactoryConfig(String bodyToReturn) implements BodyFactoryConfig
{
    @Override
    public BodyFactory createBodyFactory(final DataHeader dataHeader)
    {
        return dataRow -> TestBodyFactoryConfig.this.bodyToReturn;
    }
}
