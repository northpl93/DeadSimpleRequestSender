package pl.north93.deadsimplerequestsender.data.constant;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.data.DataPostProcessor;
import pl.north93.deadsimplerequestsender.data.DataPostProcessorConfig;

@JsonTypeName("constant")
public record ConstantPostprocessorConfig(Map<String, String> values) implements DataPostProcessorConfig
{
    @Override
    public DataPostProcessor createDataPostProcessor(final DataHeader sourceHeader)
    {
        return new ConstantPostprocessor(this.values);
    }
}
