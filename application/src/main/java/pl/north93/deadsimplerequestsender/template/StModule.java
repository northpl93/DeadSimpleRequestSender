package pl.north93.deadsimplerequestsender.template;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class StModule extends AbstractModule
{
    @Provides
    public TemplateEngine createStTemplateEngine()
    {
        return new StTemplateEngine();
    }
}
