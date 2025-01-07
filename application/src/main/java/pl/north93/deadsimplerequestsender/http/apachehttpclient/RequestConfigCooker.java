package pl.north93.deadsimplerequestsender.http.apachehttpclient;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Injector;

import org.apache.hc.core5.http.Method;

import pl.north93.deadsimplerequestsender.data.DataHeader;
import pl.north93.deadsimplerequestsender.http.BodyFactory;
import pl.north93.deadsimplerequestsender.http.RequestConfig;
import pl.north93.deadsimplerequestsender.template.Template;
import pl.north93.deadsimplerequestsender.template.TemplateEngine;

final class RequestConfigCooker
{
    private final Injector injector;
    private final TemplateEngine templateEngine;

    public RequestConfigCooker(final Injector injector, final TemplateEngine templateEngine)
    {
        this.injector = injector;
        this.templateEngine = templateEngine;
    }

    public CookedRequestConfig cookRequestConfig(final RequestConfig requestConfig, final DataHeader dataHeader)
    {
        final Template url = this.templateEngine.prepareTemplate(dataHeader, requestConfig.url());
        final Method verb = Method.normalizedValueOf(requestConfig.verb());

        final Map<String, Template> headerTemplates = new HashMap<>();
        for (final Map.Entry<String, String> entry : requestConfig.headers().entrySet())
        {
            headerTemplates.put(entry.getKey(), this.templateEngine.prepareTemplate(dataHeader, entry.getValue()));
        }

        final BodyFactory bodyFactory = requestConfig.body().createBodyFactory(this.injector, dataHeader);

        return new CookedRequestConfig(url, verb, headerTemplates, bodyFactory);
    }
}
