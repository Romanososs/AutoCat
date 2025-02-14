package com.example.autocat.network

import com.example.autocat.BuildConfig
import com.example.autocat.models.common.ErrorType
import com.example.autocat.models.dto.EmailAuthDto
import com.example.autocat.storage.PreferencesDataSource
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
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.put
import com.example.autocat.models.common.Result
import com.example.autocat.models.dto.VehicleDto
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.JsonElement

interface ApiDataSource {
    //User
    suspend fun signup(login: String, password: String): Result<EmailAuthDto>
    suspend fun login(login: String, password: String): Result<EmailAuthDto>

    //Vehicle
    suspend fun getReport(number: String): Result<VehicleDto>
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

    override suspend fun login(login: String, password: String) = withContext(Dispatchers.IO) {
        makePostRequest<EmailAuthDto>("/user/login", buildJsonObject {
            put("email", login)
            put("password", password)
        })
    }

    //Vehicle
    override suspend fun getReport(number: String) = withContext(Dispatchers.IO) {
        makeGetRequest<VehicleDto>(
            "vehicles/report", mapOf(
                "number" to number
            )
        )
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
            in 500..599 -> return Result.Error(ErrorType.SERVER)
            else -> {
                val apiResponse = httpResponse.body<ApiResponse>()
                if (!apiResponse.success) return Result.Error(ErrorType.NOT_FOUND)
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