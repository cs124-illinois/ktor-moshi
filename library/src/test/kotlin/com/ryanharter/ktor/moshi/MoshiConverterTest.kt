@file:Suppress("SpellCheckingInspection")

package com.ryanharter.ktor.moshi

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.JsonClass
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.withCharset
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.testing.testApplication
import kotlin.test.Test

class MoshiConverterTest {
    @Test
    fun reflection() =
        testApplication {
            install(ContentNegotiation) {
                moshi {
                    this.add(KotlinJsonAdapterFactory())
                }
            }
            routing {
                val foo = Foo(id = 42, name = "Foosius")
                get("/") {
                    call.respond(foo)
                }
                post("/") {
                    val request = call.receive<Foo>()
                    val text = request.toString()
                    call.respond(text)
                }
            }

            client.get("/") {
                header("Accept", "application/json")
            }.also { response ->
                assertThat(response.status).isEqualTo(HttpStatusCode.OK)
                assertThat(response.bodyAsText()).isNotNull()
                assertThat(response.bodyAsText()).isEqualTo("""{"id":42,"name":"Foosius"}""")
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
            }

            client.post("/") {
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                setBody("""{"id":43,"name":"Finnius"}""")
            }.also { response ->
                assertThat(response.status).isEqualTo(HttpStatusCode.OK)
                assertThat(response.bodyAsText()).isNotNull()
                assertThat(response.bodyAsText()).isEqualTo("Foo(id=43, name=Finnius)")
                assertThat(response.contentType()).isEqualTo(ContentType.Text.Plain.withCharset(Charsets.UTF_8))
            }
        }

    @Test
    fun codegen() =
        testApplication {
            install(ContentNegotiation) {
                moshi { }
            }
            routing {
                val bar = Bar(id = "bar-123", count = 50)
                get("/") {
                    call.respond(bar)
                }
                post("/") {
                    val request = call.receive<Bar>()
                    val text = request.toString()
                    call.respond(text)
                }
            }

            client.get("/") {
                header("Accept", "application/json")
            }.also { response ->
                assertThat(response.status).isEqualTo(HttpStatusCode.OK)
                assertThat(response.bodyAsText()).isNotNull()
                assertThat(response.bodyAsText()).isEqualTo("""{"id":"bar-123","count":50}""")
                assertThat(response.contentType()).isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
            }

            client.post("/") {
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                setBody("""{"id":"bar-543","count":-1}""")
            }.also { response ->
                assertThat(response.status).isEqualTo(HttpStatusCode.OK)
                assertThat(response.bodyAsText()).isNotNull()
                assertThat(response.bodyAsText()).isEqualTo("Bar(id=bar-543, count=-1)")
                assertThat(response.contentType()).isEqualTo(ContentType.Text.Plain.withCharset(Charsets.UTF_8))
            }
        }
}

data class Foo(
    val id: Int,
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Bar(
    val id: String,
    val count: Int,
)
