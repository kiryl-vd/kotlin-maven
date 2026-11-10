package com.example.greeting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class GreetingControllerTest {
    private val controller = GreetingController()

    @Test
    fun `greets in English`() {
        assertEquals(Greeting("Hello, Ada!"), controller.greet("Ada", "en"))
    }

    @Test
    fun `greets in German and French`() {
        assertEquals("Hallo, Ada!", controller.greet("Ada", "de").message)
        assertEquals("Bonjour, Ada!", controller.greet("Ada", "fr").message)
    }

    @Test
    fun `trims the name`() {
        assertEquals("Hello, Ada!", controller.greet("  Ada ", "en").message)
    }

    @Test
    fun `rejects a blank name`() {
        val error = assertThrows(ResponseStatusException::class.java) { controller.greet("  ", "en") }
        assertEquals(HttpStatus.BAD_REQUEST, error.statusCode)
    }

    @Test
    fun `shouts in upper case`() {
        assertEquals("HELLO, ADA!", controller.shout("Ada", "en").message)
    }
}
