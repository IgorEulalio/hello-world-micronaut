package com.igoreul.broker;

import com.igoreul.broker.model.WatchList;
import com.igoreul.broker.store.InMemoryAccountStore;
import io.micronaut.context.annotation.Executable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller("/account/watchlist-reactive")
public class WatchListControllerReactive {

    private static final Logger Log = LoggerFactory.getLogger(WatchListControllerReactive.class);

    private final InMemoryAccountStore store;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();

    public WatchListControllerReactive(InMemoryAccountStore store) {
        this.store = store;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList get(){
        Log.info("getWatchList - {}", Thread.currentThread().getName());
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(uri = "/{accountId}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList updateWatchList(@PathVariable UUID accountId, @Body WatchList watchList){
        Log.info("getWatchList - {}", Thread.currentThread().getName());
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(uri = "/{accountId}")
    @ExecuteOn(TaskExecutors.IO)
    public void delete(@PathVariable UUID accountId){
        Log.info("getWatchList - {}", Thread.currentThread().getName());
        store.deleteWatchList(accountId);
    }
}
