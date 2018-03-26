package app

import app.rest.fooGet
import app.rest.fooPost
import reactor.ipc.netty.http.server.HttpServerRoutes
import java.util.function.Consumer

fun router() = Consumer<HttpServerRoutes> { routes ->
    routes
        .get("/foo", fooGet)
        .post("/foo", fooPost)
}
