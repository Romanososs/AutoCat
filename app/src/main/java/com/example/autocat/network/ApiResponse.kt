package com.example.autocat.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ApiResponse(
    val success: Boolean,
    val data: JsonElement? = null,
    val error: String? = null
)
