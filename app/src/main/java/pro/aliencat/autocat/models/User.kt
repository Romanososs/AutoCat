package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String? = null,
    val token: String? = null
)
