package app.model

import java.time.LocalDateTime
import java.util.UUID

data class Foo(
    val id: UUID = UUID.randomUUID(),
    val created: LocalDateTime = LocalDateTime.now()
)
