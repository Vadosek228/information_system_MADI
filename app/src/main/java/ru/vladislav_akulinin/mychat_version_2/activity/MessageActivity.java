package ru.vladislav_akulinin.mychat_version_2.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vladislav_akulinin.mychat_version_2.notifications.interface_notification.APIService;
import ru.vladislav_akulinin.mychat_version_2.adapter.MessageAdapter;
import ru.vladislav_akulinin.mychat_version_2.model.ChatJava;
import ru.vladislav_akulinin.mychat_version_2.model.UserJava;
import ru.vladislav_akulinin.mychat_version_2.notifications.Client;
import ru.vladislav_akulinin.mychat_version_2.notifications.Data;
import ru.vladislav_akulinin.mychat_version_2.notifications.MyResponse;
import ru.vladislav_akulinin.mychat_version_2.notifications.Sender;
import ru.vladislav_akulinin.mychat_version_2.notifications.Token;
import ru.vladislav_akulinin.mychat_version_2.R;
import ru.vladislav_akulinin.mychat_version_2.ui.activity.MainActivity;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<ChatJava> mChatJava;

    RecyclerView recyclerView;

    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;

    String userid; //для оптимизации кода

    //для отправки уведомлений
    APIService apiService;
    boolean notify = false;

    ValueEventListener seenListener; //оказать статус сообщения (прочитано или нет)

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Нужно продумать и изменить данный код, ибо будет зависать приложение (из-за статуса)
//                //также как и в MainActivityJava
                startActivity(new Intent(MessageActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                finish();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //для вывода сообщений
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //для отправки уведомления
                notify = true;

                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Введите сообщение, чтобы отправить...", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("UserNew").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserJava userJava = dataSnapshot.getValue(UserJava.class);
                username.setText(userJava.getUsername());
                if(userJava.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
//                    Glide.with(MessageActivity.this).load(userJava.getImageURL()).into(profile_image); //нужно поместить фотографию в базуданных, которая будет по умолчанию
                    Glide.with(getApplicationContext()).load(userJava.getImageURL()).into(profile_image);
                }

                readMessage(fuser.getUid(), userid, userJava.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //вешаем звонок на сообщение
        seenMessage(userid);

    }

    //функция для отобращения статуса сообщения (прочитано/непрачитано)
    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatJava chatJava = snapshot.getValue(ChatJava.class);
                    if(chatJava.getReceiver().equals(fuser.getUid()) && chatJava.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //функция для отправки сообщений
    private void sendMessage(String sender, final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender); //отправитель
        hashMap.put("receiver", receiver); //получатель
        hashMap.put("message", message);
        hashMap.put("isseen", false); //для показа (прочитано или нет)

        reference.child("Chats").push().setValue(hashMap);

        //меняем код для лучшей производительности
        //добавляем пользователя в фрагмент чата
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //для уведомлений о сообщениях
        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("UserModel").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserJava userJava = dataSnapshot.getValue(UserJava.class);
                if(notify){
                    sendNotification(receiver, userJava.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //для отправки уведомлений
    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username+": "+message, "Новое сообщение",
                            userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Ошибка!" , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(final String myid, final String userid, final String imageurl){
        mChatJava = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatJava.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatJava chatJava = snapshot.getValue(ChatJava.class);
                    if(chatJava.getReceiver().equals(myid) && chatJava.getSender().equals(userid) ||
                            chatJava.getReceiver().equals(userid) && chatJava.getSender().equals(myid)){
                        mChatJava.add(chatJava);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChatJava,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //для пользовательского статуса
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("UserModel").child(fuser.getUid());

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

        //данное дейстиве альтернатива, но все ломается
//        try {
            reference.removeEventListener(seenListener); //для показа прочитано или нет
//        }catch (NullPointerException exc){
////            Toast.makeText(MessageActivity.this, "Загрузка данных", Toast.LENGTH_SHORT).show();
//        }

        status("offline");
    }
}
