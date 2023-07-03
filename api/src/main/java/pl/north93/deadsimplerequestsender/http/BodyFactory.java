package pl.north93.deadsimplerequestsender.http;

import pl.north93.deadsimplerequestsender.data.DataRow;

public interface BodyFactory
{
    String createBody(DataRow dataRow);
}
