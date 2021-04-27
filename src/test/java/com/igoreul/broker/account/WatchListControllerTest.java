package com.igoreul.broker.account;

import com.igoreul.broker.WatchListController;
import com.igoreul.broker.model.Quote;
import com.igoreul.broker.model.Symbol;
import com.igoreul.broker.model.WatchList;
import com.igoreul.broker.store.InMemoryAccountStore;
import com.igoreul.broker.store.InMemoryStore;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.micronaut.http.HttpRequest.DELETE;
import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@MicronautTest
public class WatchListControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject
    EmbeddedApplication application;

    @Inject
    @Client("/account/watchlist")
    RxHttpClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    void returnsEmptyWatchListForAccount() {
        final WatchList result = client.toBlocking().retrieve("/", WatchList.class);
        assertTrue(result.getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnWatchListForAcoount(){
        List<Symbol> symbols = Stream.of("APPL", "AMZN", "GOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        WatchList result = client.toBlocking().retrieve("/", WatchList.class);
        assertEquals(result.getSymbols().size(), 3);
        assertEquals(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().size(), 3);
    }

    @Test
    void canUpdateSymbolsForAccount(){
        List<Symbol> symbols = Stream.of("APPL", "AMZN", "GOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        HttpResponse<Object> response = client.toBlocking().exchange(PUT("/" + TEST_ACCOUNT_ID, watchList));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(store.getWatchList(TEST_ACCOUNT_ID), watchList);
    }

    @Test
    void canDeleteWatchListForAccount(){
        List<Symbol> symbols = Stream.of("APPL", "AMZN", "GOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        assertFalse(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
        HttpResponse<Object> response = client.toBlocking().exchange(DELETE("/" + TEST_ACCOUNT_ID));

        assertEquals(HttpStatus.OK, response.getStatus());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

}
