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
    private var userModelList: MutableList<UserModel>? = null

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val firebaseDatabase: Query = FirebaseDatabase.getInstance().reference


    @SuppressLint("PrivateResource")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        val layoutManager = LinearLayoutManager(context)
        view.recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(view.recycler_view.context, layoutManager.orientation)
        view.recycler_view.addItemDecoration(dividerItemDecoration)

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

    private fun newChat(fabCreateNewChar: View, view: View) {


//        fabCreateNewChar.setOnClickListener {
//            val input = view.findViewById(R.id.input) as EditText
//
//            firebaseDatabase
//                    .child("Chats")
//                    .push()
//                    .setValue(ChatModel(input.text.toString(),
//                            FirebaseAuth.getInstance()
//                                    .currentUser!!
//                                    .displayName)
//                    )
//
//            input.setText("")
//        }
    }

//    private fun readChats() {
//        userModelList = ArrayList<User>()
//
//        reference = FirebaseDatabase.getInstance().getReference("UserNew")
//
//        reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                mUserJavas.clear()
//
//                for (snapshot in dataSnapshot.children) {
//                    val userJava = snapshot.getValue(UserJava::class.java)
//
//                    //показывать 1 пользователя из чатов
//                    for (id in usersList) {
//                        if (userJava!!.id == id) {
//                            if (mUserJavas.isEmpty()) {
//                                for (userJava1 in mUserJavas) {
//                                    if (userJava.id != userJava1.getId()) {
//                                        mUserJavas.add(userJava)
//                                    }
//                                }
//                            } else {
//                                mUserJavas.add(userJava)
//                            }
//                        }
//                    }
//                }
//                userAdapterJava = UserAdapterJava(context, mUserJavas, true)
//                recyclerView.setAdapter(userAdapterJava)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
//    }
}