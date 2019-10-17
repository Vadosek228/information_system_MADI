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
    private lateinit var chatList: MutableList<UserModel>
    private lateinit var chatsListModel: ArrayList<ChatListModel>

    override fun getChatList(presenter: ChatPresenter){
        chatList = ArrayList()
        chatsListModel = ArrayList()
        val firebaseDatabaseChatList = FirebaseDatabase.getInstance().reference.child("Chatlist").child(mUser!!.uid)
        firebaseDatabaseChatList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatsListModel.clear()
                for(snapshot in dataSnapshot.children){
                    val chatlist = snapshot.getValue(ChatListModel::class.java)
                    chatlist?.let { chatsListModel.add(it) }
                }
                getUserList(presenter)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    fun getUserList(presenter: ChatPresenter) {
        userList = ArrayList()
        val firebaseDatabaseUser = FirebaseDatabase.getInstance().reference.child("UserNew")
        firebaseDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserModel::class.java)
                    for (chatlist: ChatListModel in chatsListModel) {
                        if (user!!.id == chatlist.id) {
                            userList.add(user)
                        }
                    }
                }
                presenter.loadChatListData(userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }
}