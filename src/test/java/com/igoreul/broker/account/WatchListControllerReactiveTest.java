package com.igoreul.broker.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.igoreul.broker.WatchListController;
import com.igoreul.broker.WatchListControllerReactive;
import com.igoreul.broker.model.Symbol;
import com.igoreul.broker.model.WatchList;
import com.igoreul.broker.store.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class WatchListControllerReactiveTest {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerReactiveTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID;

    @Inject
    EmbeddedApplication application;

    @Inject
    @Client("/account/watchlist-reactive")
    RxHttpClient client;

    @Inject
    InMemoryAccountStore store;

    @Test
    void returnsEmptyWatchListForAccount() {
        final Single<WatchList> watchListSingle = client.retrieve(GET("/"), WatchList.class).singleOrError();
        assertTrue(watchListSingle.blockingGet().getSymbols().isEmpty());
        assertTrue(store.getWatchList(TEST_ACCOUNT_ID).getSymbols().isEmpty());
    }

    @Test
    void returnWatchListForAcoount(){
        List<Symbol> symbols = Stream.of("APPL", "AMZN", "GOGL").map(Symbol::new).collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);

        store.updateWatchList(TEST_ACCOUNT_ID, watchList);

        final ObjectNode result = client.toBlocking().retrieve("/", ObjectNode.class);
        assertEquals(result.get("symbols").size(), 3);
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
