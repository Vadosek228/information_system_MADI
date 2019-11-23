package ru.vladislav_akulinin.mychat_version_2.ui.adapter.user

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

class UsersListAdapter(
        internal var context: Context?
) : RecyclerView.Adapter<UsersListAdapter.UserViewHolder>() {

    private var clickListener: OnItemClickedListener? = null
    private var userList: MutableList<User> = ArrayList()

    fun registerOnItemCallBack(clickListener: OnItemClickedListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false))
    }

    override fun getItemCount(): Int = userList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userName.text = userList[position].firstName + " " + userList[position].lastName
        holder.statusUser.text = userList[position].statusUser

        if (userList[position].status == "offline") holder.statusOff.visibility = View.VISIBLE
        else holder.statusOn.visibility = View.VISIBLE

        (holder).bind(userList[position], clickListener)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userName: TextView = itemView.findViewById(R.id.username)
        var statusUser: TextView = itemView.findViewById(R.id.status_user)
        var profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        var statusOn: CircleImageView = itemView.findViewById(R.id.img_on)
        var statusOff: CircleImageView = itemView.findViewById(R.id.img_off)

        fun bind(user: User, clickListener: OnItemClickedListener?) {
            itemView.setOnClickListener {
                clickListener?.onClicked(user)
            }
        }
    }

    fun addAll(newUser: List<User>) {
        val init = userList.size
        userList.addAll(newUser)
        notifyItemRangeChanged(init, newUser.size)
    }

    fun removeLastItem() {
        userList.removeAt(userList.size - 1)
    }
}