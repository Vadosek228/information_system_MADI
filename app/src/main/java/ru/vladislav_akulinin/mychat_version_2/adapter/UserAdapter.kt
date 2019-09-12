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
import ru.vladislav_akulinin.mychat_version_2.chat.OnItemClickedListener
import ru.vladislav_akulinin.mychat_version_2.model.UserModel

class UserAdapter(
        internal var context: Context?
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var clickListener: OnItemClickedListener? = null
    private var userModelList: MutableList<UserModel> = ArrayList()

    fun registerOnItemCallBack(clickListener: OnItemClickedListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int = userModelList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userName.text = userModelList[position].firstName + " " + userModelList[position].lastName
        holder.statusUser.text = userModelList[position].statusUser

        if (userModelList[position].status == "offline") holder.statusOff.visibility = View.VISIBLE
        else holder.statusOn.visibility = View.VISIBLE

        (holder).bind(userModelList[position], clickListener)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userName: TextView = itemView.findViewById(R.id.username)
        var statusUser: TextView = itemView.findViewById(R.id.status_user)
        var profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        var statusOn: CircleImageView = itemView.findViewById(R.id.img_on)
        var statusOff: CircleImageView = itemView.findViewById(R.id.img_off)

        fun bind(userModel: UserModel, clickListener: OnItemClickedListener?) {
            itemView.setOnClickListener {
                clickListener?.onClicked(userModel)
            }
        }
    }

    fun addAll(newUserModel: List<UserModel>) {
        val init = userModelList.size
        userModelList.addAll(newUserModel)
        notifyItemRangeChanged(init, newUserModel.size)
    }

    fun removeLastItem() {
        userModelList.removeAt(userModelList.size - 1)
    }
}