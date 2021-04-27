package com.igoreul.mostrepeated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import io.micronaut.http.client.annotation.*;
import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class MostRepeatedControllerTest {

    @Inject
    EmbeddedApplication application;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void testIndex() throws Exception {
        assertEquals(HttpStatus.OK, client.toBlocking().exchange("/3mostrepeat").status());
        List retrieve = client.toBlocking().retrieve("/3mostrepeat", List.class);
        assertEquals(2, retrieve.size());
    }
}
