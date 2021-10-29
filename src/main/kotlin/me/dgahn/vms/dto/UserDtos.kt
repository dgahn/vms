package me.dgahn.vms.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val id: String,
    val password: String
)

@Serializable
data class SignInResponse(
    val id: String
)
