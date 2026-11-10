package com.example.greeting

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/** A small REST controller. It keeps no state and calls no other system. */
@RestController
@RequestMapping("/api/greetings")
class GreetingController {
    /** Greets [name] in the language [lang] (`en`, `de` or `fr`). */
    @GetMapping("/{name}")
    fun greet(
        @PathVariable name: String,
        @RequestParam(defaultValue = "en") lang: String,
    ): Greeting {
        if (name.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The name must not be blank")
        }
        val word =
            when (lang) {
                "en" -> "Hello"
                "de" -> "Hallo"
                "fr" -> "Bonjour"
                else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown language")
            }
        return Greeting("$word, ${name.trim()}!")
    }

    /** Same as [greet], in upper case. */
    @GetMapping("/{name}/shout")
    fun shout(
        @PathVariable name: String,
        @RequestParam(defaultValue = "en") lang: String,
    ): Greeting = Greeting(greet(name, lang).message.uppercase())
}
