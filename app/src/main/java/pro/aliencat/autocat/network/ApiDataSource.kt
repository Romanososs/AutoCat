package pro.aliencat.autocat.network

import pro.aliencat.autocat.BuildConfig
import pro.aliencat.autocat.models.common.ErrorType
import pro.aliencat.autocat.dto.EmailAuthDto
import pro.aliencat.autocat.storage.PreferencesDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.path
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import pro.aliencat.autocat.models.common.Result
import pro.aliencat.autocat.dto.VehicleDto
import pro.aliencat.autocat.dto.VehicleEventDto
import pro.aliencat.autocat.dto.VehicleNoteDto
import pro.aliencat.autocat.dto.VehiclePageDto
import pro.aliencat.autocat.ui.search.model.SearchFilter

interface ApiDataSource {
    //User
    suspend fun signup(login: String, password: String): Result<EmailAuthDto>
    suspend fun login(login: String, password: String): Result<EmailAuthDto>

    //Vehicle
    suspend fun checkVehicle(number: String, notes: List<VehicleNoteDto>, events: List<VehicleEventDto>, isUpdate: Boolean = false): Result<VehicleDto>
    suspend fun getReport(number: String): Result<VehicleDto>
    suspend fun getVehiclesPage(filter: SearchFilter, pageToken: String? = null, pageSize: Int = 50): Result<VehiclePageDto>
    suspend fun getBrands(): Result<List<String>>
    suspend fun getModels(brand: String): Result<List<String>>
    suspend fun getColors(): Result<List<String>>
    suspend fun getYears(): Result<List<Int>>
//    suspend fun getRegions(): Result<List<VehicleRegion>>
}

class KtorApiDataSource(
    private val client: HttpClient,
    private val json: Json,
    preferencesDataSource: PreferencesDataSource,
) : ApiDataSource {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val baseUrl = BuildConfig.BASE_URL
    private val user = preferencesDataSource.userFlow
        .stateIn(scope, SharingStarted.Eagerly, null)

    //User
    override suspend fun signup(login: String, password: String): Result<EmailAuthDto> {
        TODO("Not yet implemented")
    }

    override suspend fun login(login: String, password: String): Result<EmailAuthDto> {
        return makePostRequest<EmailAuthDto>("/user/login", buildJsonObject {
            put("email", login)
            put("password", password)
        })
    }

    override suspend fun checkVehicle(number: String, notes: List<VehicleNoteDto>, events: List<VehicleEventDto>, isUpdate: Boolean): Result<VehicleDto> {
        //TODO add fb token
        return makePostRequest<VehicleDto>("/vehicles/check", buildJsonObject {
            put("number", number)
            put("forceUpdate", isUpdate)
            if (notes.isNotEmpty()){
                put("notes", json.encodeToJsonElement(notes))
            }
            if (events.isNotEmpty()){
                put("events", json.encodeToJsonElement(events))
            }
        })
    }

    //Vehicle
    override suspend fun getReport(number: String): Result<VehicleDto> {
        return makeGetRequest<VehicleDto>(
            "vehicles/report", mapOf(
                "number" to number
            )
        )
    }

    override suspend fun getVehiclesPage(filter: SearchFilter, pageToken: String?, pageSize: Int): Result<VehiclePageDto> {
        val params = filter.toParamMap().also {
            it["pageSize"] = pageSize.toString()
        }
        if (filter.numberQuery.isNotBlank()) params["query"] = filter.numberQuery
        pageToken?.let { params.put("pageToken", it) }
        return makeGetRequest<VehiclePageDto>("vehicles", params)
    }

    override suspend fun getBrands(): Result<List<String>> {
        return makeGetRequest<List<String>>("vehicles/brands")
    }

    override suspend fun getModels(brand: String): Result<List<String>> {
        return makeGetRequest<List<String>>("vehicles/models", mapOf("brand" to brand))
    }

    override suspend fun getColors(): Result<List<String>> {
        return makeGetRequest<List<String>>("vehicles/colors")
    }

    override suspend fun getYears(): Result<List<Int>> {
        return makeGetRequest<List<Int>>("vehicles/years")
    }

    private suspend inline fun <reified T> makeGetRequest(
        path: String,
        parameters: Map<String, String>? = null
    ): Result<T> {
        return safeCall<T> {
            executeRequest(HttpMethod.Get, path, parameters = parameters)
        }
    }

    private suspend inline fun <reified T> makePostRequest(
        path: String,
        body: JsonObject? = null
    ): Result<T> {
        return safeCall<T> {
            executeRequest(HttpMethod.Post, path, body = json.encodeToString(body))
        }
    }

    private suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T> {
        return try {
            responseToResult<T>(execute())
        } catch (e: Exception) {
            println("Api call caught an exception = ${e.message}")
            when (e) {
                is SocketTimeoutException -> Result.Error(ErrorType.TIMEOUT)
                is UnresolvedAddressException -> Result.Error(ErrorType.NETWORK)
                is SerializationException, is IllegalArgumentException -> Result.Error(ErrorType.SERIALIZATION)
                else -> Result.Error(ErrorType.UNKNOWN)
            }
        }
    }

    private suspend inline fun <reified T> responseToResult(httpResponse: HttpResponse): Result<T> {
        when (httpResponse.status.value) {
            // ...
            in 500..599 -> return Result.Error(ErrorType.SERVER_INTERNAL)
            else -> {
                val apiResponse = httpResponse.body<ApiResponse>()
                if (!apiResponse.success) return Result.Error(ErrorType.SERVER, apiResponse.error)
                return apiResponse.data?.let {
                    Result.Success(json.decodeFromJsonElement<T>(it))
                } ?: Result.Success(json.decodeFromString<T>("{}"))
            }
        }
    }

    private suspend fun executeRequest(
        methodType: HttpMethod,
        path: String,
        headers: Map<String, String>? = null,
        parameters: Map<String, String>? = null,
        body: String? = null,
    ): HttpResponse = client.request(baseUrl) {
        method = methodType
        headers {
            append(HttpHeaders.ContentType, "application/json")
            append(HttpHeaders.Accept, "application/json")
            user.value?.token?.let {
                append(HttpHeaders.Authorization, "Bearer $it")
            }
            headers?.forEach {
                append(it.key, it.value)
            }
        }
        url {
            path(path)
            parameters?.forEach {
                this.parameters.append(
                    it.key,
                    it.value
                )
            }
        }
        body?.let {
            setBody(it)
        }
    }
}