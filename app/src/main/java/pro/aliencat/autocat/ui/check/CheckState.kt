package pro.aliencat.autocat.ui.check

import pro.aliencat.autocat.models.common.LoadingState

data class CheckState(
    val number: String = "",
    val loading: LoadingState = LoadingState.Success
)
