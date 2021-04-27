package com.igoreul.broker;

import com.igoreul.broker.model.Quote;
import com.igoreul.broker.model.Symbol;
import com.igoreul.broker.store.InMemoryStore;
import com.igoreul.exception.CustomError;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.Optional;

@Controller("/quotes")
public class QuoteController {

    private final InMemoryStore memoryStore;

    public QuoteController(InMemoryStore memoryStore) {
        this.memoryStore = memoryStore;
    }

    @Get("/{symbol}")
    public HttpResponse getQuote(@PathVariable String symbol){
        Optional<Symbol> maybeQuota = memoryStore.fetchQuote(symbol);
        if (maybeQuota.isEmpty()) {
            CustomError customError = CustomError.builder()
                    .error("Quote not found!")
                    .build();

            return HttpResponse.notFound().body(customError);
        }

        return HttpResponse.ok().body(maybeQuota.get());
    }
}
