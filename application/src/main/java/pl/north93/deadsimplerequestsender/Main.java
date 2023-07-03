package pl.north93.deadsimplerequestsender;

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.north93.deadsimplerequestsender.job.JobConfig;
import pl.north93.deadsimplerequestsender.job.JobManagement;
import pl.north93.deadsimplerequestsender.job.JobModule;
import pl.north93.deadsimplerequestsender.job.YamlJobConfigLoader;
import pl.north93.deadsimplerequestsender.plugins.PluginsModule;

public class Main
{
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception
    {
        Injector injector = Guice.createInjector(new PluginsModule(), new JobModule());

        final YamlJobConfigLoader yamlJobConfigLoader = injector.getInstance(YamlJobConfigLoader.class);

        final JobConfig jobConfig = yamlJobConfigLoader.loadJobConfigFromFile(new File("P:\\Java\\DeadSimpleRequestSender\\application\\src\\main\\resources\\example-config.yaml"));
        log.info("{}", jobConfig);


        final JobManagement jobManagement = injector.getInstance(JobManagement.class);
        jobManagement.submitJob(jobConfig);
    }
}