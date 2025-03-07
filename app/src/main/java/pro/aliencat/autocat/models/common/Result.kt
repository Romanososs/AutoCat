package pro.aliencat.autocat.models.common

sealed interface Result<out D> {
    data class Success<out D>(val data: D) : Result<D>
    data class Error(val type: ErrorType, val serverMessage: String? = null) : Result<Nothing>

    fun <R> map(map: (D) -> R): Result<R> {
        return when (this) {
            is Error -> this
            is Success -> Success(map(data))
        }
    }
    fun mapToEmptyData(): Result<Unit> {
        return map { }
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T> Result<T>.onError(action: (ErrorType, String?) -> Unit): Result<T> {
    return when(this) {
        is Result.Error -> {
            action(this.type, this.serverMessage)
            this
        }
        is Result.Success -> this
    }
}