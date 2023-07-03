package pl.north93.deadsimplerequestsender.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface DataPostProcessorConfig
{
    DataPostProcessor createDataPostProcessor(DataHeader sourceHeader);
}
