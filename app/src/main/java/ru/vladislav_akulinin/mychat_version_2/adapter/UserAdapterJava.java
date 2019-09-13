package ru.vladislav_akulinin.mychat_version_2.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ru.vladislav_akulinin.mychat_version_2.ui.activity.MessageActivity;
import ru.vladislav_akulinin.mychat_version_2.model.ChatJava;
import ru.vladislav_akulinin.mychat_version_2.model.UserJava;
import ru.vladislav_akulinin.mychat_version_2.R;

//адаптер для вывода всех пользователей
public class UserAdapterJava extends RecyclerView.Adapter<UserAdapterJava.ViewHolder> {

    private Context mContext;
    private List<UserJava> mUserJavas;

    //для выбора статуса
    private boolean ischat;

    //для вывода начала сообщения в выборах чата
    String theLastMessage;

    public UserAdapterJava(Context mContext, List<UserJava> mUserJavas, boolean ischat){
        this.mContext = mContext;
        this.mUserJavas = mUserJavas;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapterJava.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UserJava userJava = mUserJavas.get(position);
        holder.username.setText(userJava.getUsername());
        if(userJava.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(userJava.getImageURL()).into(holder.profile_image);
        }

        //проверка для добавления начало сообщения в списке выбора диалогов
        if(ischat){
            lastMessage(userJava.getId(), holder.last_msg);
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }

//        для статуса пользователя
        if(ischat){
            if(userJava.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        //для вывода пользовательскийх чатов
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", userJava.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserJavas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        //для статуса
        private ImageView img_on;
        private ImageView img_off;

        //для вывода начала сообщения в выборах чата
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
//            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    //посмотрите last message (для отображения начала сообщения в списке выбора диалогов)
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatJava chatJava = snapshot.getValue(ChatJava.class);
                    if(chatJava.getReceiver().equals(firebaseUser.getUid()) && chatJava.getSender().equals(userid) ||
                            chatJava.getReceiver().equals(userid) && chatJava.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chatJava.getMessage();
                    }
                }

                switch (theLastMessage){
                    case "default":
                        last_msg.setText("Сообщений нет...(");
                        break;

                        default:
                            last_msg.setText(theLastMessage);
                            break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
