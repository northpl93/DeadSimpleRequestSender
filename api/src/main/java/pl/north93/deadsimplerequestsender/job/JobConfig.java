package pl.north93.deadsimplerequestsender.job;

import static java.util.Collections.emptyList;


import java.util.List;

import pl.north93.deadsimplerequestsender.data.DataPostProcessorConfig;
import pl.north93.deadsimplerequestsender.data.DataSourceConfig;
import pl.north93.deadsimplerequestsender.http.RequestConfig;

public record JobConfig(
        DataSourceConfig data,
        List<DataPostProcessorConfig> postProcessors,
        ExecutorConfig executor,
        RequestConfig request
) {
    public JobConfig {
        postProcessors = postProcessors == null ? emptyList() : postProcessors;
    }
}
