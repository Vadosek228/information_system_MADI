package ru.vladislav_akulinin.mychat_version_2.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import ru.vladislav_akulinin.mychat_version_2.R

class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var forgotPassword: TextView //сброс пароля

    internal lateinit var auth: FirebaseAuth

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setToolbar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.colorAccent, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorAccent)
        }

        auth = FirebaseAuth.getInstance()
        userEmail = findViewById(R.id.user_mail)
        userPass = findViewById(R.id.user_pass)
        btnLogin = findViewById(R.id.sign_in)

        btnLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val txtEmail = userEmail.text?.toString()
        val txtPassword = userPass.text?.toString()

        if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
            Toast.makeText(this@LoginActivity, "Нужно заполнить все поля", Toast.LENGTH_SHORT).show()
        } else {
            if (txtEmail != null) {
                if (txtPassword != null) {
                    auth.signInWithEmailAndPassword(txtEmail, txtPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@LoginActivity, "Не правильно набран логин или пароль", Toast.LENGTH_SHORT).show()
                                }
                            }
                }
            }
        }
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Войти в аккаунт"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
