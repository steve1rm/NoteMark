package me.androidbox.core.data.imp

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.androidbox.core.data.HttpNetworkClient

class HttpNetworkClientImp(
    private val httpClientEngine: HttpClientEngine
) : HttpNetworkClient {
    override fun build(): HttpClient {
        val httpClient = HttpClient(httpClientEngine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true

                        /** Specifies whether encounters of unknown properties in the input JSON should be ignored instead of throwing SerializationException */
                        ignoreUnknownKeys = true


                        /** JSON value is null but the property type is non-nullable.
                        Property type is an enum type, but JSON value contains an unknown enum member. */
                        coerceInputValues = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        log(message)
                    }
                }

                /** Fixme
                if(isDebug()) {
                    this.level = LogLevel.ALL
                }
                else {
                    this.level = LogLevel.NONE
                }
                **/
            }

            defaultRequest {
                this.contentType(ContentType.Application.Json)
                this.accept(ContentType.Application.Json)
            }
        }

        return httpClient
    }
}