package com.example.greeting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class GoodbyeControllerTest {
    private val controller = GoodbyeController()

    @Test
    fun `says goodbye to a name`() {
        assertEquals("Goodbye, Alice!", controller.goodbye("Alice"))
    }

    @Test
    fun `trims the name`() {
        assertEquals("Goodbye, Alice!", controller.goodbye("  Alice "))
    }

    @Test
    fun `rejects an empty name`() {
        val error = assertThrows(ResponseStatusException::class.java) { controller.goodbye("") }
        assertEquals(HttpStatus.BAD_REQUEST, error.statusCode)
    }

    @Test
    fun `rejects a blank name`() {
        val error = assertThrows(ResponseStatusException::class.java) { controller.goodbye("   ") }
        assertEquals(HttpStatus.BAD_REQUEST, error.statusCode)
    }

    @Test
    fun `rejects a missing name`() {
        val error = assertThrows(ResponseStatusException::class.java) { controller.goodbye(null) }
        assertEquals(HttpStatus.BAD_REQUEST, error.statusCode)
    }
}
