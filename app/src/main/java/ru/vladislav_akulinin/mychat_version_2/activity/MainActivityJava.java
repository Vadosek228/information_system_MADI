package ru.vladislav_akulinin.mychat_version_2.activity;

import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.vladislav_akulinin.mychat_version_2.adapter.ViewPagerAdapter;
import ru.vladislav_akulinin.mychat_version_2.fragments.ChatsFragmentJava;
import ru.vladislav_akulinin.mychat_version_2.fragments.ProfileFragmentJava;
import ru.vladislav_akulinin.mychat_version_2.fragments.UsersFragmentJava;
import ru.vladislav_akulinin.mychat_version_2.model.UserJava;
import ru.vladislav_akulinin.mychat_version_2.R;

public class MainActivityJava extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser; //предоставляет информацию о профиле пользователя, содержит методы для изменения и получения информации
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //установить toolbar для окна действий
        getSupportActionBar().setTitle("");


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //получаем текущего пользователя, вошедшего в систему
        reference = FirebaseDatabase
                .getInstance() //получить ссылку на местоположение
                .getReference("UserModel") //где хранятся
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserJava userJava = dataSnapshot.getValue(UserJava.class);
                try { //обработка исключений, если не найденны данные на сервере
                    username.setText(userJava.getUsername());
                    if(userJava.getImageURL().equals("default")){
                        profile_image.setImageResource(R.mipmap.ic_launcher); //картинка по умолчанию
                    }
                    else{
//                        Glide.with(MainActivityJava.this).load(userJava.getImageURL()).into(profile_image);
                        Glide.with(getApplicationContext()).load(userJava.getImageURL()).into(profile_image);
                    }
                }
                catch (NullPointerException exc){
                    Toast.makeText(MainActivityJava.this, "Для данного пользователя не нашлось данных", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(new ChatsFragmentJava(), "Чаты");
        viewPagerAdapter.addFragments(new UsersFragmentJava(), "Пользователи");
        viewPagerAdapter.addFragments(new ProfileFragmentJava(), "Профиль");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut(); //выйти из учетной записи
//                //Нужно продумать и изменить данный код, ибо будет зависать приложение (из-за статуса)
//                startActivity(new Intent(MainActivityJava.this, StartActivity.class)
//                                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //для пользовательского статуса
//                finish();
                return true;
        }

        return false;
    }

    //для пользовательского статуса
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("UserJava").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
