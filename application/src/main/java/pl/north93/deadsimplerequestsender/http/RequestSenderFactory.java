package pl.north93.deadsimplerequestsender.http;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.job.JobConfig;

public interface RequestSenderFactory
{
    RequestSender createRequestSender(DataHeader dataHeader, JobConfig jobConfig);
}
