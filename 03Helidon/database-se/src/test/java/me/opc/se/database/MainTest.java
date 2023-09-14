
package me.opc.se.database;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.common.http.Http;
import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.media.jsonb.JsonbSupport;
import io.helidon.common.http.Http;
import io.helidon.webclient.WebClient;
import io.helidon.webclient.WebClientResponse;
import io.helidon.webserver.WebServer;

import org.junit.jupiter.api.Order;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MainTest {

    private static final JsonBuilderFactory JSON_BUILDER = Json.createBuilderFactory(Collections.emptyMap());

    private static WebServer webServer;
    private static WebClient webClient;

    @BeforeAll
    static void startTheServer() {
        webServer = Main.startServer().await(Duration.ofSeconds(10));

        webClient = WebClient.builder()
                .baseUri("http://localhost:" + webServer.port())
                .addMediaSupport(JsonpSupport.create())
                .addMediaSupport(JsonpSupport.create())
                .addMediaSupport(JsonbSupport.create())
                .build();
    }

    @AfterAll
    static void stopServer() {
        if (webServer != null) {
            webServer.shutdown().await(10, TimeUnit.SECONDS);
        }
    }


    @Test
    void testPokemonTypes() {
        JsonArray array = webClient.get()
                .path("/type")
                .request(JsonArray.class)
                .await(Duration.ofSeconds(5));

        assertThat(array.size(), is(18));
        assertThat(array.get(0).asJsonObject().getInt("id"), is(1));
        assertThat(array.get(0).asJsonObject().getString("name"), is("Normal"));
    }

    @Test
    void testPokemons() {
        assertThat(getPokemonCount(), is(6));
        JsonObject pokemon = webClient.get()
                .path("/pokemon/1")
                .request(JsonObject.class)
                .await(Duration.ofSeconds(5));

        assertThat(pokemon.getString("name"), is("Bulbasaur"));

        pokemon = webClient.get()
                .path("/pokemon/name/Charmander")
                .request(JsonObject.class)
                .await(Duration.ofSeconds(5));

        assertThat(pokemon.getJsonNumber("id_type").intValue(), is(10));

        JsonObject json = JSON_BUILDER.createObjectBuilder()
                .add("id", 100)
                .add("idType", 1)
                .add("name", "Test")
                .build();
        WebClientResponse response = webClient.post()
                .path("/pokemon")
                .submit(json)
                .await(Duration.ofSeconds(5));

        assertThat(response.status(), is(Http.Status.OK_200));
        assertThat(getPokemonCount(), is(7));

        response =  webClient.delete()
                .path("/pokemon/100")
                .request()
                .await(Duration.ofSeconds(5));

        assertThat(response.status(), is(Http.Status.OK_200));
        assertThat(getPokemonCount(), is(6));
    }

    private int getPokemonCount() {
        JsonArray array = webClient.get()
                .path("/pokemon")
                .request(JsonArray.class)
                .await(Duration.ofSeconds(5));
        return array.size();
    }

    @Test
    void testMicroprofileMetrics() {
        String get = webClient.get()
                .path("/simple-greet/greet-count")
                .request(String.class)
                .await(Duration.ofSeconds(5));

        assertThat(get, containsString("Hello World!"));

        String openMetricsOutput = webClient.get()
                .path("/metrics")
                .request(String.class)
                .await(Duration.ofSeconds(5));

        assertThat("Metrics output", openMetricsOutput, containsString("application_accessctr_total"));
    }

    @Test
    void testMetrics() {
        WebClientResponse response = webClient.get()
                .path("/metrics")
                .request()
                .await(Duration.ofSeconds(5));
        assertThat(response.status().code(), is(200));
    }

    @Test
    void testHealth() {
        WebClientResponse response = webClient.get()
                .path("health")
                .request()
                .await(Duration.ofSeconds(5));
        assertThat(response.status().code(), is(200));
    }

    @Test
    void testSimpleGreet() {
        JsonObject jsonObject = webClient.get()
                                         .path("/simple-greet")
                                         .request(JsonObject.class)
                                         .await(Duration.ofSeconds(5));
        assertThat(jsonObject.getString("message"), is("Hello World!"));
    }
}
