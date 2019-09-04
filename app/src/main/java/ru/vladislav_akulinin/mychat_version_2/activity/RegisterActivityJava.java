package ru.vladislav_akulinin.mychat_version_2.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import ru.vladislav_akulinin.mychat_version_2.R;
import ru.vladislav_akulinin.mychat_version_2.ui.activity.MainActivity;

public class RegisterActivityJava extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //установить toolbar для окна действий
        getSupportActionBar().setTitle("Реистрация");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //кнопка возврата на предыдущую активность

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        //гарантирует, что есть только один экземпляр, предоставляет к нему глобальную точку доступа
        auth = FirebaseAuth.getInstance(); //создание и возврат объекта (возвращает экзепляр класса)

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString(); //присваиваем значение переменной из ...
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                //для выполнения операций над String// проверка на пустоту
                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivityJava.this, "Должны быть заполнены все поля", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivityJava.this, "Длина пароля должна быть больше 6 символов", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);
                }
            }
        });
    }

    private void register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password) //создает новую учетную запись пользователя по логину и паролю
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //для обработки успешного и неуспешного ответа слушателя
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { //по завершению// Task - для параллельного действия потоков
                        if(task.isSuccessful()){ //возвращает true, если задача успешно завершена
                            FirebaseUser firebaseUser = auth.getCurrentUser(); //получение информации о текущем пользователе
                            assert firebaseUser != null; //обнаружение некорректных данных (завершает программу, после обнаружения некорректных данных)
                            String userid = firebaseUser.getUid();//получаем идентификатор текущего пользователя


                            reference = FirebaseDatabase.getInstance().getReference("UserJava") //получение корневой ссылки, где будут храниться
                                    .child(userid); //является дочерним узлом

                            HashMap<String, String> hashMap = new HashMap<>(); //хэш-таблица для хранения, обеспечивает быстрое выполнение запросов
                            //интерфейс Map - хранение данных в виде пар ключ/значение
                            hashMap.put("id", userid); //добавление элемента по ключ/значениям
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase()); //перевод имени в имя с маленькой букой

                            //получаем дополнительную информацию для установки значения зависимого атрибута
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivityJava.this, MainActivity.class);
                                        //таск для вызываемого Activity будет очищен, а вызываемое Activity станет в нем корневым.
                                        //станет значением новой задачи в стеке итсории
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//применение флагов
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivityJava.this, "Вы не можете зарегистрироваться с данным Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
