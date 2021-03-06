package com.igoreul.broker;

import com.igoreul.broker.model.WatchList;
import com.igoreul.broker.store.InMemoryAccountStore;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public class WatchListController {

    private final InMemoryAccountStore store;
    public static final UUID ACCOUNT_ID = UUID.randomUUID();

    public WatchListController(InMemoryAccountStore store) {
        this.store = store;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get(){
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(uri = "/{accountId}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public WatchList updateWatchList(@PathVariable UUID accountId, @Body WatchList watchList){

        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(uri = "/{accountId}")
    public void delete(@PathVariable UUID accountId){

        store.deleteWatchList(accountId);
    }
}
