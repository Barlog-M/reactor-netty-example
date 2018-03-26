package app.rest

import app.Server
import app.extension.ServerExtension
import app.model.Foo
import app.objectMapper
import io.netty.handler.codec.http.HttpResponseStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Mono
import reactor.ipc.netty.http.client.HttpClient
import reactor.test.StepVerifier
import java.time.Duration

@ExtendWith(ServerExtension::class)
class FooTest {

    @Test
    fun get() {
        StepVerifier.create(
            HttpClient
                .create(Server.host(), Server.port())
                .get("/foo")
                .flatMap { r ->
                    assertEquals(HttpResponseStatus.OK.code(), r.status().code())
                    r.receive().aggregate().asString()
                }
                .timeout(Duration.ofMillis(1000)))
            .assertNext { data -> data.isNotBlank() }
            .verifyComplete()
    }

    @Test
    fun post() {
        StepVerifier.create(
            HttpClient
                .create(Server.host(), Server.port())
                .post("/foo", { r ->
                    r.sendString(
                        Mono.fromCallable {
                            objectMapper.writeValueAsString(Foo())
                        })
                })
                .map { it.status().code() }
                .timeout(Duration.ofMillis(1000)))
            .assertNext { it == HttpResponseStatus.OK.code() }
            .verifyComplete()
    }

    @Test
    fun postBadRequest() {
        StepVerifier.create(
            HttpClient
                .create(Server.host(), Server.port())
                .post("/foo", { r ->
                    r.sendString(Mono.just("{a: b}"))
                })
                .timeout(Duration.ofMillis(1000)))
            .expectError()
            .verify()
    }
}
