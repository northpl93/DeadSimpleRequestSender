package pl.north93.deadsimplerequestsender.data;

import com.google.common.base.Preconditions;

record DataRowViewImpl(Object[] backingValues, int offset, int size) implements DataRowView
{
    @Override
    public Object getValue(final int index)
    {
        Preconditions.checkArgument(index < this.size);
        return this.backingValues[this.offset + index];
    }

    @Override
    public void setValue(final int index, final Object value)
    {
        Preconditions.checkArgument(index < this.size);
        this.backingValues[this.offset + index] = value;
    }
}
