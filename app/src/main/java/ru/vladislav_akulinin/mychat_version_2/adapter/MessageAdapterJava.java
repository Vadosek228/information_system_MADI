package ru.vladislav_akulinin.mychat_version_2.adapter;

import android.content.Context;

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

import java.util.List;

import ru.vladislav_akulinin.mychat_version_2.model.ChatJava;
import ru.vladislav_akulinin.mychat_version_2.R;

public class MessageAdapterJava extends RecyclerView.Adapter<MessageAdapterJava.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<ChatJava> mChatJava;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapterJava(Context mContext, List<ChatJava> mChatJava, String imageurl){
        this.mContext = mContext;
        this.mChatJava = mChatJava;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapterJava.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapterJava.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapterJava.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterJava.ViewHolder holder, int position) {

        ChatJava chatJava = mChatJava.get(position);

        holder.show_message.setText(chatJava.getMessage());

        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

        //для отображения статуса сообщения (прочитано или нет)
        if(position == mChatJava.size()-1){
            if(chatJava.isIsseen()){
                holder.txt_seen.setText("Прочитано");
            }else {
                holder.txt_seen.setText("Не прочитано");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChatJava.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen; //для отображения (прочитано или нет) сообщения пользователя

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //устанавливаем кто отправил сообщение
        if(mChatJava.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}

