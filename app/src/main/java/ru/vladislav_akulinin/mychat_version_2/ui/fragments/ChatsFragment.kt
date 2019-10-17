package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chats.view.*
import ru.vladislav_akulinin.mychat_version_2.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.toolbar.*
import ru.vladislav_akulinin.mychat_version_2.adapter.chat.OnItemClickedListener
import ru.vladislav_akulinin.mychat_version_2.adapter.user.UsersListAdapter
import ru.vladislav_akulinin.mychat_version_2.model.ChatListModel
import ru.vladislav_akulinin.mychat_version_2.model.ChatModel
import ru.vladislav_akulinin.mychat_version_2.model.UserModel
import ru.vladislav_akulinin.mychat_version_2.mvp.chat.ChatInterface
import ru.vladislav_akulinin.mychat_version_2.mvp.chat.ChatPresenter
import ru.vladislav_akulinin.mychat_version_2.ui.activity.MainActivity


class ChatsFragment : Fragment(), OnItemClickedListener, ChatInterface.View {
//    private lateinit var chatAdapter: ChatListAdapter
    private lateinit var chatAdapter: UsersListAdapter
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private lateinit var mUser: MutableList<ChatModel>
    private lateinit var chatsListModel: ArrayList<ChatListModel>
    private lateinit var chatList: MutableList<ChatModel>

    var total_item = 0
    var last_visibe_item = 0
    var isLoading = false

//    private lateinit var chatViewModel: ChatViewModel

    private lateinit var userList: MutableList<UserModel>
    private var presenterChat: ChatPresenter ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        val parentActivity : MainActivity = activity as MainActivity // parent reference
        parentActivity.setToolbar()
        parentActivity.toolbar.setTitle(R.string.menu_chats)

        val layoutManager = LinearLayoutManager(context)
        view.recycler_view_list_chat.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(view.recycler_view_list_chat.context, layoutManager.orientation)
        view.recycler_view_list_chat.addItemDecoration(dividerItemDecoration)

        chatsListModel = ArrayList()

        userList = ArrayList()

//        chatAdapter = ChatListAdapter(context)
        chatAdapter = UsersListAdapter(context)
//        userAdapter.registerOnItemCallBack(this)
        view.recycler_view_list_chat.adapter = chatAdapter

        presenterChat = ChatPresenter(this)
        presenterChat?.let {
            it.getChatList(it)
        }

        createNewChat(view)

        view.recycler_view_list_chat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                total_item = layoutManager.itemCount
                last_visibe_item = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && total_item <= last_visibe_item) {
//                    chatList(view)
                    isLoading = true
                }
            }
        })

        return view
    }

    override fun initViewChat(){
        presenterChat?.getChatList(presenterChat!!)
    }

    override fun getChatList(loadChatList: MutableList<UserModel>) {
        chatAdapter.addAll(loadChatList)
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

    @SuppressLint("PrivateResource")
    private fun openChat(chatModel: ChatModel){
        val intent = Intent().putExtra("userid", chatModel.id)
        fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.container, MessageFragment(intent))
                .addToBackStack(null)
                .commit()
    }

    override fun onClicked(chatModel: ChatModel) {
        openChat(chatModel)
    }

    override fun onLongClicked(chatModel: ChatModel): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}