package me.androidbox.core.data.imp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.data.TokenDto
import me.androidbox.authentication.login.domain.model.TokenRefresh
import me.androidbox.core.data.HttpNetworkClient
import me.androidbox.core.data.Routes
import me.androidbox.core.models.Constants.EMAIL_STEVE

class HttpNetworkClientImp(
    private val noteMarkPreferences: NoteMarkPreferences
) : HttpNetworkClient {
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
                this.logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d {
                            message
                        }
                    }
                }
                this.level = LogLevel.ALL

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
                this.header("X-User-Email", EMAIL_STEVE)
            }

            install(Auth) {
                this.bearer {
                    this.loadTokens {
                        /** Load tokens from shared preferences */
                        BearerTokens(
                            accessToken = noteMarkPreferences.getAccessToken() ?: "",
                            refreshToken = noteMarkPreferences.getRefreshToken() ?: ""
                        )
                    }

                    this.refreshTokens {
                        val requestBearerTokens = this.client.post(Routes.TOKEN_REFRESH) {
                            this.setBody(
                                TokenRefresh(
                                    refreshToken = noteMarkPreferences.getRefreshToken() ?: "",
                                )
                            )
                            this.markAsRefreshTokenRequest()

                            // Fixme
                         //   if(isDebug()) {
                                this.header("Debug", true)
                          //  }
                        }.body<TokenDto?>()

                        /** Save updated token to the cache */
                        if(requestBearerTokens != null) {
                            /** Save updated tokens to cache */
                            noteMarkPreferences.setAccessToken(requestBearerTokens.accessToken)
                            noteMarkPreferences.setRefreshToken(requestBearerTokens.refreshToken)

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