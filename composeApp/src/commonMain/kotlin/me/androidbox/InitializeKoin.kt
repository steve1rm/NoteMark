package me.androidbox

import io.kotzilla.sdk.analytics.koin.analytics
import me.androidbox.di.noteMarkModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initializeKoin(
    config: KoinAppDeclaration? = null,
    vararg platformSpecificModules: Module = emptyArray()) {

    startKoin {
        config?.invoke(this)
        modules(
            noteMarkModule,
            *platformSpecificModules
        )
        analytics {
            setApiKey("ktz-sdk-ue7AdrDm5l2-O0fp8e-7UUyOcSDSaBkUZzKdJUdWXXs")
            setVersion("1.0.0")
        }
    }
}
