package pl.north93.deadsimplerequestsender.http;

import pl.north93.deadsimplerequestsender.data.DataHeader;

public interface RequestSenderFactory
{
    RequestSender createRequestSender(DataHeader dataHeader, RequestSenderConfig requestSenderConfig);
}
