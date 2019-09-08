package ru.vladislav_akulinin.mychat_version_2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.User

class UserAdapter(internal var context: Context?): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int = userList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userName.text = userList[position].firstName + " " + userList[position].lastName
        holder.statusUser.text = userList[position].statusUser

        if(userList[position].status == "offline") holder.statusOff.visibility = View.VISIBLE
        else holder.statusOn.visibility = View.VISIBLE
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var userName: TextView = itemView.findViewById(R.id.username)
        internal var statusUser: TextView = itemView.findViewById(R.id.status_user)
        internal var profileImage: ImageView = itemView.findViewById(R.id.profile_image)

        internal var statusOn: CircleImageView = itemView.findViewById(R.id.img_on)
        internal var statusOff: CircleImageView = itemView.findViewById(R.id.img_off)
    }

    fun addAll(newUser:List<User>){
        val init = userList.size
        userList.addAll(newUser)
        notifyItemRangeChanged(init, newUser.size)
    }

    fun removeLastItem(){
        userList.removeAt(userList.size - 1)
    }
}