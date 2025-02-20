package pl.north93.deadsimplerequestsender.job;

public class JobDefinitionParsingException extends RuntimeException
{
    public JobDefinitionParsingException(final Throwable cause)
    {
        super("Failed to parse job definition", cause);
    }
}
