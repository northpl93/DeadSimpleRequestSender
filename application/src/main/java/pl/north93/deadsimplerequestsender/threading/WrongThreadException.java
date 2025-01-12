package pl.north93.deadsimplerequestsender.threading;

public final class WrongThreadException extends RuntimeException
{
    public WrongThreadException(final String message)
    {
        super(message);
    }
}
