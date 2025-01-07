package pl.north93.deadsimplerequestsender.http.apachehttpclient;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

import pl.north93.deadsimplerequestsender.http.RequestSenderFactory;
import pl.north93.deadsimplerequestsender.template.TemplateEngine;

public final class RequestSenderModule extends AbstractModule
{
    @Provides
    public RequestSenderFactory createRequestSenderFactory(final Injector injector, final TemplateEngine templateEngine)
    {
        final RequestConfigCooker requestConfigCooker = new RequestConfigCooker(injector, templateEngine);
        return new ApacheHttpClientRequestSenderFactory(requestConfigCooker);
    }
}
