package pro.aliencat.autocat.repositories

import pro.aliencat.autocat.models.User
import pro.aliencat.autocat.models.common.Result
import pro.aliencat.autocat.network.ApiDataSource
import pro.aliencat.autocat.storage.PreferencesDataSource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>

    suspend fun signup(login: String, password: String): Result<User>
    suspend fun login(login: String, password: String): Result<User>
    suspend fun logout()
}

class UserRepositoryImpl(
    private val prefDataSource: PreferencesDataSource,
    private val apiDataSource: ApiDataSource
): UserRepository {
    override fun getUser(): Flow<User> = prefDataSource.userFlow

    override suspend fun signup(login: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun login(login: String, password: String): Result<User> {
        return apiDataSource.login(login, password).map {
            User(email = it.email, token = it.token)
        }.also {
            if (it is Result.Success) prefDataSource.updateUser(it.data)
        }
    }

    override suspend fun logout() {
        prefDataSource.updateUser(null)
    }

}