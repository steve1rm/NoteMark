package me.androidbox.core.data.imp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.androidbox.core.data.HttpNetworkClient
import me.androidbox.core.data.Routes

class HttpNetworkClientImp : HttpNetworkClient {
    override fun build(): HttpClient {
        val httpClient = HttpClient(CIO) {
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

            install(Auth) {
                this.bearer {
                    this.loadTokens {
                        /** Load tokens from shared preferences */
                        BearerTokens(
                            accessToken = "",
                            refreshToken = ""
                        )
                    }

                    this.refreshTokens {
                        val requestBearerTokens = this.client.post(Routes.TOKEN_REFRESH) {
                            this.setBody(
                                RequestRefreshTokens(
                                    grantType = "refresh_token",
                                    refreshToken = "",
                                    clientId = "",
                                    clientSecret = ""
                                )
                            )
                        }.body<LoginResponseDto?>()


                        /** Save updated token to the cache */
                        if(requestBearerTokens != null) {
                            val authorization = AuthorizationInfo(
                                accessToken = requestBearerTokens.accessToken,
                                refreshToken = requestBearerTokens.refreshToken
                            )

                            /** Save updated tokens to cache */

                            /** Update tokens */
                            BearerTokens(
                                accessToken = requestBearerTokens.accessToken,
                                refreshToken = requestBearerTokens.refreshToken
                            )
                        }
                        else {
                            /** Just return empty as request failed to get tokens */
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                }
            }
        }

        return httpClient
    }
}

data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String
)

data class RequestRefreshTokens(
    val grantType: String,
    val refreshToken: String,
    val clientId: String,
    val clientSecret: String
)

data class AuthorizationInfo(
    val accessToken: String,
    val refreshToken: String
)