package it.skrape.ktor

import io.ktor.server.testing.TestApplicationResponse
import it.skrape.core.Doc
import it.skrape.core.Parser

/**
 * Will convert a TestApplicationResponse body to a parsed Document.
 * Thereby it will give you the possibility to check HTML or XML from within a Ktor-test.
 */
fun TestApplicationResponse.expectHtml(init: Doc.() -> Unit): Doc {
    val response = this.content ?: throw IllegalArgumentException("can not parse document of content null")
    return Parser(response).parse().also(init)
}
