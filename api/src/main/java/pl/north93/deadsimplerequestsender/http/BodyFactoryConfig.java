package pl.north93.deadsimplerequestsender.http;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.inject.Injector;

import pl.north93.deadsimplerequestsender.data.DataHeader;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface BodyFactoryConfig
{
    BodyFactory createBodyFactory(Injector injector, DataHeader dataHeader);
}
