package com.igoreul.broker;

import com.igoreul.broker.model.Symbol;
import com.igoreul.broker.store.InMemoryStore;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;
import java.util.List;

@Controller("/markets")
public class MarketController {

    private final InMemoryStore memoryStore;

    public MarketController(InMemoryStore memoryStore) {
        this.memoryStore = memoryStore;
    }

    @Get("/")
    public List<Symbol> all(){
        return memoryStore.getAllSymbols();
    }
}
