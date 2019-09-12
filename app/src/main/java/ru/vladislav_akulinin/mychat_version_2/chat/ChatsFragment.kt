package ru.vladislav_akulinin.mychat_version_2.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chats.view.*
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.adapter.UserAdapter
import ru.vladislav_akulinin.mychat_version_2.model.UserModel
import com.google.firebase.database.FirebaseDatabase


class ChatsFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter

    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    private lateinit var usersList: MutableList<String>
    private lateinit var mUser: MutableList<UserModel>


    @SuppressLint("PrivateResource")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        val layoutManager = LinearLayoutManager(context)
        view.recycler_view_list_chat.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(view.recycler_view_list_chat.context, layoutManager.orientation)
        view.recycler_view_list_chat.addItemDecoration(dividerItemDecoration)

        usersList = ArrayList()
        newChat(view)

        val fabCreateNewChar: View = view.findViewById(R.id.fab_create_chat)
        fabCreateNewChar.setOnClickListener {
            fragmentManager!!
                    .beginTransaction()
                    .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                    .replace(R.id.container, AddChatFragment())
                    .addToBackStack(null)
                    .commit()
        }

        return view
    }

    private fun newChat(view: View) {

        val firebaseDatabase: Query = FirebaseDatabase.getInstance().reference.child("Chats")
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear()

                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(MessageModel::class.java)

                    if (chat!!.sender == firebaseUser?.uid) {
                        chat.receiver?.let { usersList.add(it) }
                    }
                    if (chat.receiver == firebaseUser?.uid) {
                        chat.sender?.let { usersList.add(it) }
                    }
                }

                readChats(view)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun readChats(view: View) {
        mUser = ArrayList()
        val firebaseDatabase: Query = FirebaseDatabase.getInstance().reference.child("UserNew")

        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUser.clear()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserModel::class.java)

                    //показывать 1 пользователя из чатов
                    for (id in usersList) {
                        if (user!!.id == id) {
                            if (mUser.isEmpty()) {
                                for (userModel in mUser) {
                                    if (user.id != userModel.id) {
                                        mUser.add(user)
                                    }
                                }
                            } else {
                                mUser.add(user)
                            }
                        }
                    }
                }
                userAdapter = UserAdapter(context)
                view.recycler_view_list_chat.adapter = userAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}