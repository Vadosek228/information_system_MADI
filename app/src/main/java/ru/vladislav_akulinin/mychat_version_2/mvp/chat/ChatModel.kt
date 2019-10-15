package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.vladislav_akulinin.mychat_version_2.model.ChatListModel
import ru.vladislav_akulinin.mychat_version_2.model.UserModel

class ChatModel : ChatInterface.Model {
    private val mUser = FirebaseAuth.getInstance().currentUser
    private lateinit var userList: MutableList<UserModel>
    private lateinit var chatsListModel: ArrayList<ChatListModel>

    //получить весь список пользователей
    override fun getUserList(presenter: ChatPresenter): MutableList<UserModel> {
        userList = ArrayList()
        chatsListModel = ArrayList()
        val firebaseDatabaseUser = FirebaseDatabase.getInstance().reference.child("UserNew")
        firebaseDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserModel::class.java)
//                    for (chatlist: ChatListModel in chatsListModel) {
//                        if (user!!.id == chatlist.id) {
                            if (user != null) {
                                userList.add(user)
//                            }
//                        }
                    }
                }
                presenter.loadUserListData(userList)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

        return userList
    }
}