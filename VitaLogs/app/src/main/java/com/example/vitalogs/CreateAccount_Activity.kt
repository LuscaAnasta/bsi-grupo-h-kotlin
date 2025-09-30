package com.example.vitalogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccount_Activity : AppCompatActivity() {

    // Referências para os componentes da UI
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var cpfEditText: EditText
    private lateinit var buttonCreate: Button
    private lateinit var buttonReturn: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // O layout XML deve ser o estilizado que você criou
        setContentView(R.layout.activity_create_account)

        // Inicializa os componentes da tela
        buttonReturn = findViewById(R.id.btn_return)
        buttonCreate = findViewById(R.id.btn_CreateAccount)
        nameEditText = findViewById(R.id.et_Name)
        emailEditText = findViewById(R.id.et_Email)
        passwordEditText = findViewById(R.id.et_Password)
        cpfEditText = findViewById(R.id.et_Cpf)
        progressBar = findViewById(R.id.progressBar) // Certifique-se que o ProgressBar foi adicionado no XML

        // Ação do botão de voltar
        buttonReturn.setOnClickListener {
            val intent = Intent(this@CreateAccount_Activity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Ação do botão de criar conta
        buttonCreate.setOnClickListener {
            performRegistration()
        }
    }

    /**
     * Controla a visibilidade do indicador de loading e o estado do botão.
     * @param isLoading Boolean que indica se a animação de loading deve ser exibida.
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            buttonCreate.isEnabled = false
            buttonCreate.text = "Criando conta..."
        } else {
            progressBar.visibility = View.GONE
            buttonCreate.isEnabled = true
            buttonCreate.text = "Create Account"
        }
    }

    /**
     * Valida os campos e inicia a chamada de rede para o cadastro.
     */
    private fun performRegistration() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val cpf = cpfEditText.text.toString().trim()

        // Validação simples para campos vazios
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cpf.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true) // Mostra o loading antes de fazer a chamada

        val call = RetrofitClient.instance.cadastrarUsuario(name, email, password, cpf)

        call.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                showLoading(false) // Esconde o loading quando a resposta chega

                if (response.isSuccessful) {
                    val serverResponse = response.body()
                    Toast.makeText(this@CreateAccount_Activity, serverResponse?.message, Toast.LENGTH_LONG).show()

                    if (serverResponse?.status == "success") {
                        // Em CreateAccount_Activity.kt
                        val intent = Intent(this@CreateAccount_Activity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // O servidor respondeu com um código de erro (ex: 404, 500)
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e("CADASTRO_ERRO", "Código: $errorCode, Mensagem: $errorMessage")
                    Toast.makeText(this@CreateAccount_Activity, "Erro no cadastro (Código: $errorCode).", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                showLoading(false) // Esconde o loading em caso de falha de conexão
                Log.e("CADASTRO_FALHA", "Falha detalhada na conexão: ", t)
                Toast.makeText(this@CreateAccount_Activity, "Falha na conexão. Verifique o Logcat.", Toast.LENGTH_LONG).show()
            }
        })
    }
}