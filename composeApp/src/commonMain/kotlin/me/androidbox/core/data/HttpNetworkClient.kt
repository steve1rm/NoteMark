package me.androidbox.core.data

import io.ktor.client.HttpClient

interface HttpNetworkClient {
    fun build(): HttpClient
}