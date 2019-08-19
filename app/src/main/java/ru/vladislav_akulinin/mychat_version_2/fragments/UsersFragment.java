package ru.vladislav_akulinin.mychat_version_2.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.vladislav_akulinin.mychat_version_2.adapter.UserAdapter;
import ru.vladislav_akulinin.mychat_version_2.model.UserJava;
import ru.vladislav_akulinin.mychat_version_2.R;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<UserJava> mUserJavas;

    EditText search_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserJavas = new ArrayList<>();

        readUsers();

        //поле поиска пользователя
        search_user = view.findViewById(R.id.search_user);
        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    //для поиска пользователя
    private void searchUser(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserJavas.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserJava userJava = snapshot.getValue(UserJava.class);

                    assert userJava != null;
                    assert fuser != null;
                    if(!userJava.getId().equals(fuser.getUid())){
                        mUserJavas.add(userJava);
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUserJavas, false);
                recyclerView.setAdapter(userAdapter); //установить новое значение в ресайкл из поиска
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (search_user.getText().toString().equals("")) { //если значение поисковой строки равно нулю
                    mUserJavas.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserJava userJava = snapshot.getValue(UserJava.class);

                        assert userJava != null;
                        assert firebaseUser != null;
                        if (!userJava.getId().equals(firebaseUser.getUid())) {
                            mUserJavas.add(userJava);
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUserJavas, false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
