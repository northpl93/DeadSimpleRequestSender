package pl.north93.deadsimplerequestsender.data;

public interface DataRowView
{
    Object getValue(int index);

    void setValue(int index, Object value);

    int size();
}
