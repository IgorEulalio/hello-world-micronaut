package com.igoreul.broker;

import com.igoreul.broker.model.Quote;
import com.igoreul.broker.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@MicronautTest
public class QuoteControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteControllerTest.class);

    @Inject
    EmbeddedApplication application;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    InMemoryStore store;

    @Test
    public void returnsQuotePerSymbol(){
        final Quote quote = client.toBlocking().retrieve(HttpRequest.GET("/quotes/APPL"), Quote.class);
//        Need to implement methods to achieve inMemoryStore return
        LOG.info("Received Quota {}", quote.getSymbol());
    }

    @Test
    public void returnsQuoteNotFound(){
        final Quote quote = client.toBlocking().retrieve(HttpRequest.GET("/quotes/UNSUPPORTED_SYMBOL"), Quote.class);
    }


}
