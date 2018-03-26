package app.rest

import app.model.Foo
import app.objectMapper
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.netty.handler.codec.http.HttpResponseStatus
import mu.KotlinLogging
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger("FooEndpoint")

val fooGet = Handler { _, resp ->
    resp
        .addHeader(CONTENT_TYPE, APPLICATION_JSON)
        .status(HttpResponseStatus.OK)
        .sendString(Mono.fromCallable { objectMapper.writeValueAsString(Foo()) })
}

val fooPost = Handler { req, resp ->
    req
        .receive()
        .aggregate()
        .asByteArray()
        .map {
            objectMapper.readValue(it, Foo::class.java)
        }
        .flatMap {
            logger.info { "POST $it" }
            Mono.empty<Void>()
        }
        .onErrorResume {
            resp.status(HttpResponseStatus.BAD_REQUEST).send()
        }
}
