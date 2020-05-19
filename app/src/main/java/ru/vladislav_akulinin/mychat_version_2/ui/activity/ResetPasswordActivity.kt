package ru.vladislav_akulinin.mychat_version_2.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import ru.vladislav_akulinin.mychat_version_2.R

@SuppressLint("Registered")
class ResetPasswordActivity() : AppCompatActivity(), Parcelable { //пароль присылается на почту администратору базы данных (мне) - это настраивается на Firebase!!!

    private lateinit var firebaseAuth: FirebaseAuth

    constructor(parcel: Parcel) : this()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) //установить toolbar для окна действий
        supportActionBar!!.title = "Сброс пароля"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //кнопка возврата на предыдущую активность


        firebaseAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            val email = findViewById<TextView>(R.id.send_email).text.toString()

            if (email == "") {
                Toast.makeText(this@ResetPasswordActivity, "Необходимо заполнить все поля", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ResetPasswordActivity, "Пожалуйста проверьте Email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
                    } else {
                        val error = task.exception!!.message
                        Toast.makeText(this@ResetPasswordActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResetPasswordActivity> {
        override fun createFromParcel(parcel: Parcel): ResetPasswordActivity {
            return ResetPasswordActivity(parcel)
        }

        override fun newArray(size: Int): Array<ResetPasswordActivity?> {
            return arrayOfNulls(size)
        }
    }
}