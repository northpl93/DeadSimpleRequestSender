package pl.north93.deadsimplerequestsender.template;

import pl.north93.deadsimplerequestsender.data.DataRow;

public interface Template
{
    String render(DataRow dataRow);
}
