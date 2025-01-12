package pl.north93.deadsimplerequestsender.messaging;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface CommandHandler<COMMAND extends Command<RESPONSE>, RESPONSE>
{
    RESPONSE handleCommand(COMMAND command);

    @SuppressWarnings("unchecked")
    default Class<COMMAND> supportedCommand()
    {
        for (final Type type : this.getClass().getGenericInterfaces())
        {
            if (type instanceof final ParameterizedType parameterizedType)
            {
                return (Class<COMMAND>) parameterizedType.getActualTypeArguments()[0];
            }
        }

        throw new IllegalStateException();
    }
}
