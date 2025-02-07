package pl.north93.deadsimplerequestsender.data;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.inject.Injector;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface DataSourceConfig
{
    DataSource createDataSource(File localWorkDir, Injector injector);
}
