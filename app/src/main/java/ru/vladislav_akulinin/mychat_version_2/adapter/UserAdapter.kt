package ru.vladislav_akulinin.mychat_version_2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.User
import ru.vladislav_akulinin.mychat_version_2.model.UserList


class UserAdapter(internal var context: Context?): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    internal var userList: MutableList<UserList>

    val lastItemId: String?
        get() = userList[userList.size - 1].id

    init {
        this.userList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userName.text = userList[position].firstName
        holder.statusUser.text = userList[position].lastName
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var userName: TextView
        internal var statusUser: TextView

        init {
            userName = itemView.findViewById(R.id.username)
            statusUser = itemView.findViewById(R.id.status_user)
        }

    }

    fun addAll(newUser:List<UserList>){
        val init = userList.size
        userList.addAll(newUser)
        notifyItemRangeChanged(init, newUser.size)
    }

    fun removeLastItem(){
        userList.removeAt(userList.size - 1)
    }
}