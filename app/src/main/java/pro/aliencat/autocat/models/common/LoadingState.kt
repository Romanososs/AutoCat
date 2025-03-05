package pro.aliencat.autocat.models.common

sealed class LoadingState{
    data object Loading:LoadingState()
    data object Success:LoadingState()
    data class Error(val message: String? = null):LoadingState()
    data object Empty:LoadingState()
}

//enum class LoadingState{
//    Loading,
//    Success,
//    Error,
//    Empty
//}
