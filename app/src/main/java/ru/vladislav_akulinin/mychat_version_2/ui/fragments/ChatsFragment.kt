package ru.vladislav_akulinin.mychat_version_2.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chats.view.*
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.adapter.UserAdapter
import ru.vladislav_akulinin.mychat_version_2.model.UserModel
import com.google.firebase.database.FirebaseDatabase
import ru.vladislav_akulinin.mychat_version_2.model.ChatListModel


class ChatsFragment : Fragment(), OnItemClickedListener {
    private lateinit var userAdapter: UserAdapter

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var mUser: MutableList<UserModel>
    private lateinit var chatsListModel: ArrayList<ChatListModel>

    var total_item = 0
    var last_visibe_item = 0
    var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        val layoutManager = LinearLayoutManager(context)
        view.recycler_view_list_chat.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(view.recycler_view_list_chat.context, layoutManager.orientation)
        view.recycler_view_list_chat.addItemDecoration(dividerItemDecoration)

        chatsListModel = ArrayList()

        userAdapter = UserAdapter(context)
        userAdapter.registerOnItemCallBack(this)
        view.recycler_view_list_chat.adapter = userAdapter

        chatList(view)
        createNewChat(view)

        view.recycler_view_list_chat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                total_item = layoutManager.itemCount
                last_visibe_item = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && total_item <= last_visibe_item) {
                    chatList(view)
                    isLoading = true
                }
            }
        })

        return view
    }

    @SuppressLint("PrivateResource")
    private fun createNewChat(view: View){
        val fabCreateNewChar: View = view.findViewById(R.id.fab_create_chat)
        fabCreateNewChar.setOnClickListener {
            fragmentManager!!
                    .beginTransaction()
                    .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                    .replace(R.id.container, AddChatFragment())
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun chatList(view: View){
        val firebaseDatabaseChatList = FirebaseDatabase.getInstance().reference.child("Chatlist").child(firebaseUser!!.uid)
        firebaseDatabaseChatList.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatsListModel.clear()
                for(snapshot in dataSnapshot.children){
                    val chatlist = snapshot.getValue(ChatListModel::class.java)
                    chatlist?.let { chatsListModel.add(it) }
                }
                userList()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun userList() {
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
                userAdapter.addAll(mUser)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    @SuppressLint("PrivateResource")
    private fun openChat(userModel: UserModel){
        val intent = Intent().putExtra("userid", userModel.id)
        fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.container, MessageFragment(intent))
                .addToBackStack(null)
                .commit()
    }

    override fun onClicked(userModel: UserModel) {
        openChat(userModel)
    }

    override fun onLongClicked(userModel: UserModel): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}