package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.vladislav_akulinin.mychat_version_2.model.ChatListModel
import ru.vladislav_akulinin.mychat_version_2.model.MessageModel
import ru.vladislav_akulinin.mychat_version_2.model.UserModel
import ru.vladislav_akulinin.mychat_version_2.model.Chat

class ChatModel : ChatInterface.Model {
    private val mUser = FirebaseAuth.getInstance().currentUser
    private lateinit var userList: MutableList<UserModel>
    private lateinit var chatList: MutableList<Chat>
    private lateinit var chatsListModel: ArrayList<ChatListModel>
    private lateinit var messageModelList: MutableList<MessageModel>

    override fun getChatList(presenter: ChatPresenter) {
        chatsListModel = ArrayList()
        val firebaseDatabaseChatList = FirebaseDatabase.getInstance().reference.child("Chatlist").child(mUser!!.uid)
        firebaseDatabaseChatList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatsListModel.clear()
                for (snapshot in dataSnapshot.children) {
                    val chatlist = snapshot.getValue(ChatListModel::class.java)
                    chatlist?.let { chatsListModel.add(it) }
                }
                getUserList(presenter)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun getUserList(presenter: ChatPresenter) {
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
                getMessageList(userList, presenter)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }

    private fun getMessageList(userList: MutableList<UserModel>, presenter: ChatPresenter) {
        messageModelList = ArrayList()
        chatList = ArrayList()

        val firebaseReferenceChat = FirebaseDatabase.getInstance().reference.child("Chats")
        firebaseReferenceChat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageModelList.clear()
                for (userid in userList) {
                    var messageModel_2: MessageModel ?= null
                    for (snapshot in dataSnapshot.children) {
                        val messageModel = snapshot.getValue(MessageModel::class.java)
                        if (messageModel!!.receiver == mUser?.uid && messageModel.sender == userid.id || messageModel.receiver == userid.id && messageModel.sender == mUser?.uid) {
                            messageModelList.add(messageModel)
                            messageModel_2 = messageModel
                        }
                    }
                    chatList.add(Chat(
                            userid.id,
                            messageModel_2?.sender,
                            messageModel_2?.receiver,
                            userid.firstName + " " + userid.lastName + " " + userid.fatherName,
                            "",
                            messageModel_2?.message,
                            messageModel_2?.isseen
                    ))
                }

                presenter.loadChatListData(chatList)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}