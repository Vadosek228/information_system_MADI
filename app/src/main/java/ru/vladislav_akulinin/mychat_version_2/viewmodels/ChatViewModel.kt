package ru.vladislav_akulinin.mychat_version_2.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.vladislav_akulinin.mychat_version_2.model.ChatListModel
import ru.vladislav_akulinin.mychat_version_2.model.UserModel

class ChatViewModel : ViewModel() {

    //список всех пользователей
    //список всех чатов
    //список всех сообщений по чату

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var chatsListModel: ArrayList<ChatListModel>
    private lateinit var mUser: MutableList<UserModel>

    //получить список чатов пользователя
    fun getChatList(){
        val firebaseDatabaseChatList = FirebaseDatabase.getInstance().reference.child("Chatlist").child(firebaseUser!!.uid)
        firebaseDatabaseChatList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatsListModel.clear()
                for(snapshot in dataSnapshot.children){
                    val chatlist = snapshot.getValue(ChatListModel::class.java)
                    chatlist?.let { chatsListModel.add(it) }
                }
//                userList()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    //получить весь список пользователей
    fun getFirebaseUserList() : MutableList<UserModel>{
        mUser = ArrayList()
        val firebaseDatabaseUser = FirebaseDatabase.getInstance().reference.child("UserNew")
        firebaseDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserModel::class.java)
                    for (chatlist: ChatListModel in chatsListModel) {
                        if (user!!.id == chatlist.id) {
                            mUser.add(user)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return mUser
    }

}