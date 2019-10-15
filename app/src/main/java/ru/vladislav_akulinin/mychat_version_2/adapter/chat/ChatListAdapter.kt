package ru.vladislav_akulinin.mychat_version_2.adapter.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.ChatModel

class ChatListAdapter(
        internal var context: Context?
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    private var clickListener: OnItemClickedListener? = null
    private var chatModelList: MutableList<ChatModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false))
    }

    override fun getItemCount(): Int = chatModelList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.userName.text = chatModelList[position].userName
        holder.statusUser.text = chatModelList[position].userMessage

        if (chatModelList[position].userStatus == "offline") holder.statusOff.visibility = View.VISIBLE
        else holder.statusOn.visibility = View.VISIBLE

        (holder).bind(chatModelList[position], clickListener)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userName: TextView = itemView.findViewById(R.id.username)
        var statusUser: TextView = itemView.findViewById(R.id.status_user)
        var profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        var statusOn: CircleImageView = itemView.findViewById(R.id.img_on)
        var statusOff: CircleImageView = itemView.findViewById(R.id.img_off)

        fun bind(chatModel: ChatModel, clickListener: OnItemClickedListener?) {
            itemView.setOnClickListener {
                clickListener?.onClicked(chatModel)
            }
        }
    }

    fun addAll(newUserModel: MutableList<ChatModel>) {
        val init = chatModelList.size
        chatModelList.addAll(newUserModel)
        notifyItemRangeChanged(init, newUserModel.size)
    }

    fun removeLastItem() {
        chatModelList.removeAt(chatModelList.size - 1)
    }

}