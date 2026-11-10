package com.example.greeting

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/** A small REST controller for goodbye messages. It keeps no state and calls no other system. */
@RestController
@RequestMapping("/goodbye")
class GoodbyeController {
    /** Says goodbye to [name]. */
    @GetMapping
    fun goodbye(
        @RequestParam(required = false) name: String?,
    ): String {
        if (name.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The name must not be blank")
        }
        return "Goodbye, ${name.trim()}!"
    }
}
