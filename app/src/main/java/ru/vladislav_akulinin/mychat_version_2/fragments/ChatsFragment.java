package ru.vladislav_akulinin.mychat_version_2.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import ru.vladislav_akulinin.mychat_version_2.adapter.UserAdapterJava;
import ru.vladislav_akulinin.mychat_version_2.model.Chat;
import ru.vladislav_akulinin.mychat_version_2.model.UserJava;
import ru.vladislav_akulinin.mychat_version_2.notifications.Token;
import ru.vladislav_akulinin.mychat_version_2.R;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapterJava userAdapterJava;
    private List<UserJava> mUserJavas;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList; //оптимизируем код
//    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        //заменяем данный код (для оптимизации)
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fuser.getUid())){
                        usersList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fuser.getUid())){
                        usersList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
//                    usersList.add(chatlist);
//                }
//
//                chatList();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        //для отправки уведомлений
        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    //для отправки уведомлений
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

//    private void chatList() {
//
//        mUserJavas = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("UserJava");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUserJavas.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    UserJava user = snapshot.getValue(UserJava.class);
//                    for(Chatlist chatlist : usersList){
//                        if(user.getId().equals(chatlist.getId())){
//                            mUserJavas.add(user);
//                        }
//                    }
//                }
//                userAdapterJava = new UserAdapterJava(getContext(), mUserJavas,true);
//                recyclerView.setAdapter(userAdapterJava);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

//    заменяем данный код (для оптимизации)
    private void readChats(){ //читать сообщения
        mUserJavas = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("UserNew");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserJavas.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserJava userJava = snapshot.getValue(UserJava.class);

                    //показывать 1 пользователя из чатов
                    for(String id : usersList){
                        if (userJava.getId().equals(id)){
                            if(mUserJavas.isEmpty()){
                                for(UserJava userJava1 : mUserJavas){
                                    if(!userJava.getId().equals(userJava1.getId())){
                                        mUserJavas.add(userJava);
                                    }
                                }
                            } else {
                                mUserJavas.add(userJava);
                            }
                        }
                    }
                }
                                                                        //try - для статуса
                userAdapterJava = new UserAdapterJava(getContext(), mUserJavas, true);
                recyclerView.setAdapter(userAdapterJava);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
}
