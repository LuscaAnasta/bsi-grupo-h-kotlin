package com.example.vitalogs

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }

    // !!! ATENÇÃO !!!
    // Coloque aqui o IP da sua máquina que está rodando o servidor PHP.
    // Lembre-se que o celular/emulador precisa estar na mesma rede.
    private const val BASE_URL = "http://10.135.246.12/"
}