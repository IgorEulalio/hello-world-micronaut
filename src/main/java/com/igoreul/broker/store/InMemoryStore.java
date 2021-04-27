package com.igoreul.broker.store;

import com.igoreul.broker.model.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);

    private List<Symbol> symbols;
    private final ThreadLocalRandom current = ThreadLocalRandom.current();

    public InMemoryStore() {
        symbols = Stream.of("APPL","GOOG", "FBOK34", "AMZB", "MSFT", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }

    public Optional<Symbol> fetchQuote(String symbol) {
        if (symbols.stream().filter(s -> s.getValue().equalsIgnoreCase(symbol)).findFirst().isEmpty()) {
            throw new EntityNotFoundException();
        }

        return symbols.stream().filter(s -> s.getValue().equalsIgnoreCase(symbol)).findFirst();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(current.nextDouble(1, 100));
    }
}
