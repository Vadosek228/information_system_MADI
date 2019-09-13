package ru.vladislav_akulinin.mychat_version_2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.MessageModel

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    val MSG_TYPE_LEFT = 0
    val MSG_TYPE_RIGHT = 1

    private var mContext: Context? = null
    private lateinit var mChat: MutableList<MessageModel>
    private var imageurl: String? = null

    internal var fuser: FirebaseUser? = null

    fun MessageAdapter(mContext: Context?, mChatJava: MutableList<MessageModel>, imageurl: String?) {
        this.mContext = mContext
        this.mChat = mChatJava
        this.imageurl = imageurl
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MSG_TYPE_RIGHT) {
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false)
            return ViewHolder(view)
        } else {
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chatJava = mChat[position]

        holder.show_message.text = chatJava.message

        if (imageurl == "default") {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher)
        } else {
            mContext?.let { Glide.with(it).load(imageurl).into(holder.profile_image) }
        }

        //для отображения статуса сообщения (прочитано или нет)
        if (position == mChat.size - 1) {
            if (chatJava.isseen!!) {
                holder.txt_seen.text = "Прочитано"
            } else {
                holder.txt_seen.text = "Не прочитано"
            }
        } else {
            holder.txt_seen.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var show_message: TextView
        var profile_image: ImageView
        var txt_seen: TextView //для отображения (прочитано или нет) сообщения пользователя

        init {

            show_message = itemView.findViewById(R.id.show_message)
            profile_image = itemView.findViewById(R.id.profile_image)
            txt_seen = itemView.findViewById(R.id.txt_seen)
        }
    }

    override fun getItemViewType(position: Int): Int {
        fuser = FirebaseAuth.getInstance().currentUser
        //устанавливаем кто отправил сообщение
        return if (mChat[position].sender == fuser!!.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }
}