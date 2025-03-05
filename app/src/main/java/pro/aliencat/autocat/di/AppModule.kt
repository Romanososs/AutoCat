package pro.aliencat.autocat.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import pro.aliencat.autocat.network.ApiDataSource
import pro.aliencat.autocat.network.KtorApiDataSource
import pro.aliencat.autocat.repositories.UserRepository
import pro.aliencat.autocat.repositories.UserRepositoryImpl
import pro.aliencat.autocat.repositories.VehicleRepository
import pro.aliencat.autocat.repositories.VehicleRepositoryImpl
import pro.aliencat.autocat.storage.PreferencesDataSource
import pro.aliencat.autocat.storage.PreferencesStoreDataSource
import pro.aliencat.autocat.network.SearchPagingDataSourceImpl
import pro.aliencat.autocat.ui.history.HistoryViewModel
import pro.aliencat.autocat.ui.check.CheckViewModel
import pro.aliencat.autocat.ui.search.SearchViewModel
import pro.aliencat.autocat.ui.settings.SettingsViewModel
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
import pro.aliencat.autocat.database.AppDatabase
import pro.aliencat.autocat.network.SearchPagingDataSource

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

    single<AppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "autocat.db"
        ).build()
    }
    single { get<AppDatabase>().vehicleDao() }

    singleOf(::PreferencesStoreDataSource).bind<PreferencesDataSource>()
    singleOf(::KtorApiDataSource).bind<ApiDataSource>()
    single<SearchPagingDataSource> { SearchPagingDataSourceImpl(get()) }

    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::VehicleRepositoryImpl).bind<VehicleRepository>()

    viewModelOf(::HistoryViewModel)
    viewModelOf(::CheckViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
//    viewModelOf(::FilterListViewModel)

}