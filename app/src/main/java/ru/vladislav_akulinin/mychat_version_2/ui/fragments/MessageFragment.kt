package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_message.view.*
import kotlinx.android.synthetic.main.toolbar.*

import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.adapter.message.MessageAdapter
import ru.vladislav_akulinin.mychat_version_2.model.MessageModel
import ru.vladislav_akulinin.mychat_version_2.model.User
import ru.vladislav_akulinin.mychat_version_2.ui.activity.MainActivity
import ru.vladislav_akulinin.mychat_version_2.utils.Utils.hideKeyboard
import java.text.SimpleDateFormat
import java.util.*

class MessageFragment(intent: Intent) : Fragment() {

    val intent = intent
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    var firebaseReference = FirebaseDatabase.getInstance().reference

    var user: User ?= null
    private lateinit var otherUser: String

    private lateinit var messageModelList: MutableList<MessageModel>
    internal lateinit var messageAdapter: MessageAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_message, container, false)

        val parentActivity : MainActivity = activity as MainActivity // parent reference
        parentActivity.setToolbar()
        parentActivity.toolbar.title = "Приватный чат"

        view.recycler_view_message.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        view.recycler_view_message.layoutManager = linearLayoutManager

        val userId = intent.getStringExtra("userid")
        otherUser = userId

        getUserData()
        createMessage(view)

        return view
    }

    private fun createMessage(view: View) {
        val userId = intent.getStringExtra("userid")

        view.btn_send.setOnClickListener {
            val msg = view.text_send.text.toString()
            if (msg != "") {
                sendMessage(firebaseUser!!.uid, userId, msg)
                view.isFocusableInTouchMode = true
                view.requestFocus()
            } else {
                Toast.makeText(context, "Введите сообщение, чтобы отправить...", Toast.LENGTH_SHORT).show()
            }
            view.text_send.setText("")
        }

        val firebaseReferenceUser = firebaseReference.child("UserNew").child(userId)

        firebaseReferenceUser.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                view.username.text = "${user?.firstName} ${user?.lastName} - ${user?.statusUser}"
                if (user?.imageURL == "default") {
                    view.profile_image.setImageResource(R.drawable.avatar_default)
                } else {
                    context?.let { Glide.with(it).load(user?.imageURL).into(view.profile_image) }
                }

                readMessage(firebaseUser!!.uid, userId, user?.imageURL, view)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

//    override fun onStart() {
//        super.onStart()
//        seenMessage(otherUser)
//    }

    private fun getUserData() {
        val firebaseDatabaseUser = FirebaseDatabase.getInstance().reference.child("UserNew")
        firebaseDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val userNew = snapshot.getValue(User::class.java)
                    if (userNew != null && firebaseUser!!.uid == userNew.id) {
                        user = userNew
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendMessage(sender: String, receiver: String, message: String){

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message
        hashMap["isseen"] = false
        hashMap["author"] = user?.firstName + " " + user?.lastName + " " + user?.fatherName
        hashMap["time"] = currentDate.toString()

        firebaseReference.child("Chats").push().setValue(hashMap)

        //добавляем пользователя в фрагмент чата
        val userid = intent.getStringExtra("userid")
        val chatRef = firebaseReference.child("Chatlist")
                .child(firebaseUser!!.uid)
                .child(userid!!)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userid)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        hideKeyboard()
    }

    private fun readMessage(myid: String?, userid: String?, imageurl: String?, view: View) {
        messageModelList = ArrayList()

        val firebaseReferenceChat = FirebaseDatabase.getInstance().reference.child("Chats")
        firebaseReferenceChat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageModelList.clear()
                for (snapshot in dataSnapshot.children) {
                    val messageModel = snapshot.getValue(MessageModel::class.java)
                    if (messageModel!!.receiver == myid && messageModel.sender == userid || messageModel.receiver == userid && messageModel.sender == myid) {
                        messageModelList.add(messageModel)
                    }

                    messageAdapter = MessageAdapter()
                    messageAdapter.MessageAdapter(context, messageModelList, imageurl)
                    view.recycler_view_message.adapter = messageAdapter
                    view.recycler_view_message.scrollToPosition(messageModelList.size - 1)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun seenMessage(userid: String?) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val messageModel = snapshot.getValue(MessageModel::class.java)
                    if (messageModel!!.receiver == firebaseUser?.uid && messageModel.sender == userid) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["isseen"] = true
                        snapshot.ref.updateChildren(hashMap)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
