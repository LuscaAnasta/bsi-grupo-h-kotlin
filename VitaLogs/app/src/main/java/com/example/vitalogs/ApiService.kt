package com.example.vitalogs

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("meu_projeto_api/login.php")
    fun login(
        @Query("usuario") usuario: String,
        @Query("senha") senha: String
    ): Call<List<User>>

    @FormUrlEncoded
    @POST("meu_projeto_api/cadastrar.php")
    fun cadastrarUsuario(
        @Field("nome") nome: String,
        @Field("email") email: String,
        @Field("senha") senha: String,
        @Field("cpf") cpf: String // <-- NOVO CAMPO ADICIONADO
    ): Call<GenericResponse>
}