package com.example.vitalogs

import com.google.gson.annotations.SerializedName

// A classe agora se chama User, representando um usuário
data class User(
    @SerializedName("usuarioId")
    val id: Int,

    @SerializedName("usuarioNome")
    val nome: String,

    @SerializedName("usuarioEmail")
    val email: String,

    @SerializedName("usuarioCpf")
    val cpf: String?
)