package app

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue

class ServerTest {
    @Test
    fun start() {
        Server.start("localhost")
        assertTrue(Server.isStarted())
        Server.stop()
    }
}
