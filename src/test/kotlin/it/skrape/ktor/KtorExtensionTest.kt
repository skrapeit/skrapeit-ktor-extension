package it.skrape.ktor

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.ktor.server.testing.TestApplicationResponse
import it.skrape.matchers.toBe
import it.skrape.matchers.toBeNotPresent
import it.skrape.matchers.toBePresent
import it.skrape.selects.element
import it.skrape.selects.elements
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class KtorExtensionTest {

    private val aValidHtmlString = """
        <expectHtml>
            <head>
                <title>i'm the title</title>
            </head>
            <body>
                <h1>headline</h1>
                <ul>
                    <li>item1</li>
                    <li>item2</li>
                    <li>item3</li>
                </ul>
            </body>
        </expectHtml>
    """.trimIndent()

    @Test
    fun `can parse and check a TestApplicationResponse's content`() {

        val response = mock<TestApplicationResponse> {
            on { content } doReturn aValidHtmlString
        }

        response.expectHtml {
            title() toBe "i'm the title"
            element("h1").text() toBe "headline"
            elements("li").size toBe 3
            element("body").toBePresent()
            elements(".not-existing").toBeNotPresent()
        }
    }

    @Test
    fun `can store a parsed TestApplicationResponse's content`() {

        val response = mock<TestApplicationResponse> {
            on { content } doReturn aValidHtmlString
        }

        val doc = response.expectHtml {}
        assertAll {
            assertThat(doc.title()).isEqualTo("i'm the title")
            assertThat(doc.select("h1")).isEqualTo("headline")
        }
    }

    @Test
    fun `can handle null response`() {

        val response = mock<TestApplicationResponse> {
            on { content } doReturn null
        }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            response.expectHtml {}
        }
    }

}