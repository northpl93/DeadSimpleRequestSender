package pl.north93.deadsimplerequestsender.http;

import pl.north93.deadsimplerequestsender.data.DataRow;

public interface RequestSender extends AutoCloseable
{
    void sendRequest(DataRow dataRow) throws RequestSendingException;
}
