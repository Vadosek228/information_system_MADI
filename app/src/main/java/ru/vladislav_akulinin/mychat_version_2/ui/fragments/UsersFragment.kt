package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.adapter.UserAdapter
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.fragment_users.view.*
import ru.vladislav_akulinin.mychat_version_2.chat.ChatViewModel
import ru.vladislav_akulinin.mychat_version_2.chat.OnItemClickedListener
import ru.vladislav_akulinin.mychat_version_2.model.UserModel


class UsersFragment : Fragment() {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

    private lateinit var userAdapter: UserAdapter
    private var userList = ArrayList<UserModel>()

    var total_item = 0
    var last_visibe_item = 0
    var isLoading = false

    companion object {
        const val USER_PATH_KEY = "UserNew"
        const val SEARCH_KEY = "search"
    }

//    private var chatListViewModel: ChatViewModel? = null
//    private var inSearchMode: Boolean = false
//
//    private val onUserListClickedListener =
//            object : OnItemClickedListener<ChatViewModel> {
//                override fun onClicked(item: ChatViewModel) {
//                    chatListViewModel?.onTapItem(item)
//                }
//
//                override fun onLongClicked(item: ChatViewModel): Boolean {
//                    return if (!inSearchMode) {
//                        chatListViewModel?.onLongTapItem(item)
//                        true
//                    } else
//                        false
//                }
//            }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_users, container, false)

        val layoutManager = LinearLayoutManager(context)
        view.recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(view.recycler_view.context, layoutManager.orientation)
        view.recycler_view.addItemDecoration(dividerItemDecoration)

        userAdapter = UserAdapter(context)
        view.recycler_view.adapter = userAdapter

        getUsers()

        view.recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                total_item = layoutManager.itemCount
                last_visibe_item = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && total_item <= last_visibe_item) {
                    getUsers()
                    isLoading = true
                }
            }
        })

        //поле поиска пользователя
        view.search_user.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                searchUser(charSequence.toString().toLowerCase(), view)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        return view
    }

    //для поиска пользователя
    private fun searchUser(search: String, view: View) {
        val query: Query = FirebaseDatabase.getInstance().reference
                .child(USER_PATH_KEY)
                .orderByChild(SEARCH_KEY)
                .startAt(search)
                .endAt(search + "\uf8ff")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                userList.clear()

                for (snapshot in p0.children) {
                    val user = snapshot.getValue(UserModel::class.java)!!

                    if (user.id != firebaseUser?.uid) {
                        userList.add(user)
                    }
                }

                userAdapter = UserAdapter(context)
                view.recycler_view.adapter = userAdapter
                userAdapter.addAll(userList)
                isLoading = false
            }
        })

    }

    private fun getUsers() {
        val query: Query = FirebaseDatabase.getInstance().reference
                .child(USER_PATH_KEY)
                .orderByKey()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                isLoading = if(p0.hasChildren()) {
                    for (snapshot in p0.children) {
                        val user = snapshot.getValue(UserModel::class.java)!!

                        if (user.id != firebaseUser?.uid) {
                            userList.add(user)
                        }
                    }
                    userAdapter.addAll(userList)
                    false
                } else {
                    false
                }
            }
        })
    }

    //обновление данных
    private fun refreshData() {
        userAdapter.removeLastItem()
        userAdapter.notifyDataSetChanged()
        getUsers()
    }

}