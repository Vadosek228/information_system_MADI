package ru.vladislav_akulinin.mychat_version_2.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.vladislav_akulinin.mychat_version_2.R

class StartActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        findViewById<Button>(R.id.login).setOnClickListener{
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.register).setOnClickListener{
            startActivity(Intent(this@StartActivity, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        //получаем текущего пользователя, который вошел в систему
        firebaseUser = FirebaseAuth.getInstance().currentUser

        //если пользователь уже вошел в систему, то переправляем его на новую активность (чтобы не авторизовываться постоянно)
        if (firebaseUser != null) {
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}