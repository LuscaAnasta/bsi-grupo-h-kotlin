package com.example.vitalogs

import android.content.Intent
import android.os.Bundle
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

class LoginActivity : AppCompatActivity() {

    // Referências para os componentes da UI
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var buttonReturn: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa os componentes da tela
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        buttonReturn = findViewById(R.id.btn_return)
        progressBar = findViewById(R.id.progressBar)

        // Ação do botão de login
        loginButton.setOnClickListener {
            performLogin()
        }

        // Ação do botão de voltar para a tela inicial
        buttonReturn.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Controla a visibilidade do indicador de loading e o estado do botão.
     * @param isLoading Boolean que indica se a animação de loading deve ser exibida.
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false
            loginButton.text = "Entrando..." // Muda o texto do botão
        } else {
            progressBar.visibility = View.GONE
            loginButton.isEnabled = true
            loginButton.text = "Login" // Restaura o texto original
        }
    }

    /**
     * Valida os campos e inicia a chamada de rede para o login.
     */
    private fun performLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validação simples para campos vazios
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true) // Mostra o loading antes de fazer a chamada

        val call = RetrofitClient.instance.login(email, password)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                showLoading(false) // Esconde o loading quando a resposta chega

                if (response.isSuccessful) {
                    val userList = response.body()
                    if (userList != null && userList.isNotEmpty()) {
                        // Sucesso no login, navegar para a próxima tela
                        val loggedInUser = userList[0]
                        Toast.makeText(this@LoginActivity, "Login bem-sucedido, ${loggedInUser.nome}!", Toast.LENGTH_LONG).show()

                        // TODO: Substituir MainActivity::class.java pela sua tela de Dashboard
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Resposta do servidor veio, mas a lista de usuários está vazia (login inválido)
                        Toast.makeText(this@LoginActivity, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // O servidor respondeu com um código de erro (ex: 404, 500)
                    Toast.makeText(this@LoginActivity, "Erro na resposta do servidor. Código: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                showLoading(false) // Esconde o loading em caso de falha de conexão
                Toast.makeText(this@LoginActivity, "Falha na conexão: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}