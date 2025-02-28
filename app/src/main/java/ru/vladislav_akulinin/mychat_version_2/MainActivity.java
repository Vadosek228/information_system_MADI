package ru.vladislav_akulinin.mychat_version_2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.images.ImageRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.vladislav_akulinin.mychat_version_2.Adapter.ViewPagerAdapter;
import ru.vladislav_akulinin.mychat_version_2.Fragments.ChatsFragment;
import ru.vladislav_akulinin.mychat_version_2.Fragments.ProfileFragment;
import ru.vladislav_akulinin.mychat_version_2.Fragments.UsersFragment;
import ru.vladislav_akulinin.mychat_version_2.Model.User;

public class MainActivity extends AppCompatActivity {

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
                .getReference("User") //где хранятся
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try { //обработка исключений, если не найденны данные на сервере
                    username.setText(user.getUsername());
                    if(user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.mipmap.ic_launcher); //картинка по умолчанию
                    }
                    else{
//                        Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }
                }
                catch (NullPointerException exc){
                    Toast.makeText(MainActivity.this, "Для данного пользователя не нашлось данных", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(new ChatsFragment(), "Чаты");
        viewPagerAdapter.addFragments(new UsersFragment(), "Пользователи");
        viewPagerAdapter.addFragments(new ProfileFragment(), "Профиль");

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
                startActivity(new Intent(MainActivity.this, StartActivity.class)
                                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //для пользовательского статуса
//                finish();
                return true;
        }

        return false;
    }

    //для пользовательского статуса
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

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
