package ru.vladislav_akulinin.mychat_version_2.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_new.*
import ru.vladislav_akulinin.mychat_version_2.R
import java.util.HashMap


class RegisterActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private var auth : FirebaseAuth ?= null
    private var reference: DatabaseReference ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new)
        setToolbar()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.colorAccent, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorAccent)
        }

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener{
            registerSing()
        }
    }

    private fun registerSing(){
        val firstName = tv_first_name?.text?.toString()
        val lastName = tv_last_name?.text?.toString()
        val fatherName = tv_father_name.text.toString()
        val phone = tv_phone.text.toString()
        val userStatus = spinner_status.selectedItem.toString()
        val email = tv_email?.text?.toString()
        val password = et_password?.text?.toString()
        val passwordCheck = et_password_check?.text?.toString()

        if (firstName.isNullOrEmpty() || lastName.isNullOrEmpty() || email.isNullOrEmpty()
                || password.isNullOrEmpty() || passwordCheck.isNullOrEmpty()) {
            Toast.makeText(this@RegisterActivity, "Должны быть заполнены Фамилия, Имя, Почта и Пароль", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(this@RegisterActivity, "Длина пароля должна быть больше 6 символов", Toast.LENGTH_SHORT).show()
        } else if (password != passwordCheck){
            Toast.makeText(this@RegisterActivity, "Пароль не совпадает", Toast.LENGTH_SHORT).show()
        } else {
            register(firstName, lastName, fatherName, phone, email, userStatus, password)
        }
    }

    private fun register(first_name: String, last_name: String, father_name: String, phone: String, email: String, userStatus: String, password: String)
    {
        auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth?.currentUser!! //получение информации о текущем пользователе
                        val userid = firebaseUser.uid

                        reference = FirebaseDatabase.getInstance().getReference("UserNew").child(userid)

                        val hashMap = HashMap<String, String>() //хэш-таблица для хранения, обеспечивает быстрое выполнение запросов
                        //интерфейс Map - хранение данных в виде пар ключ/значение
                        hashMap["id"] = userid
                        hashMap["firstName"] = first_name
                        hashMap["lastName"] = last_name
                        hashMap["fatherName"] = father_name
                        hashMap["phoneNumber"] = phone
                        hashMap["email"] = email
                        hashMap["statusUser"] = userStatus
                        hashMap["imageURL"] = "default"
                        hashMap["status"] = "offline"
                        hashMap["search"] = first_name.toLowerCase() //перевод имени в имя с маленькой букой
                        hashMap["about"] = ""
                        hashMap["course"] = "Не указано"
                        hashMap["group"] = "Не указано"

                        //получаем дополнительную информацию для установки значения зависимого атрибута
                        reference!!.setValue(hashMap).addOnCompleteListener { refer ->
                            if (refer.isSuccessful) {
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Вы не можете зарегистрироваться с данным Email", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.register_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Регистрация"
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