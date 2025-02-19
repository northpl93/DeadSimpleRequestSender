package pl.north93.deadsimplerequestsender.rest.templates;

import com.google.inject.Inject;

import io.javalin.http.Context;

public final class TemplateResource
{
    private final TemplatesLoader templatesLoader;

    @Inject
    TemplateResource(final TemplatesLoader templatesLoader)
    {
        this.templatesLoader = templatesLoader;
    }

    public void getTemplates(final Context ctx) throws Exception
    {
        ctx.json(this.templatesLoader.getTemplates());
    }
}
