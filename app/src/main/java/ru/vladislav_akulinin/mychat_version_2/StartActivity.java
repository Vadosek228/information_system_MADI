package ru.vladislav_akulinin.mychat_version_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button register, login;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        //инициализируем объект и получаем текущего пользователя (чтобы не авторизовываться постоянно)
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //получаем текущего пользователя, который вошел в систему

        //если пользователь уже вошел в систему, то переправляем его на новую активность (чтобы не авторизовываться постоянно)
        if(firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });
    }
}
