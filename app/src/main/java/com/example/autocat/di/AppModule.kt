package com.example.autocat.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.autocat.network.ApiDataSource
import com.example.autocat.network.KtorApiDataSource
import com.example.autocat.repositories.UserRepository
import com.example.autocat.repositories.UserRepositoryImpl
import com.example.autocat.repositories.VehicleRepository
import com.example.autocat.repositories.VehicleRepositoryImpl
import com.example.autocat.storage.PreferencesDataSource
import com.example.autocat.storage.PreferencesStoreDataSource
import com.example.autocat.ui.history.HistoryViewModel
import com.example.autocat.ui.settings.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val PREFERENCES_NAME = "preferences"

val appModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(get<Json>())
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
    single {
        PreferenceDataStoreFactory.create(
            corruptionHandler = null,
            migrations = listOf(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { androidApplication().preferencesDataStoreFile(PREFERENCES_NAME) }
        )
    }


    singleOf(::PreferencesStoreDataSource).bind<PreferencesDataSource>()
    singleOf(::KtorApiDataSource).bind<ApiDataSource>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::VehicleRepositoryImpl).bind<VehicleRepository>()

    viewModelOf(::HistoryViewModel)
    viewModelOf(::SettingsViewModel)

}