package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_users.view.*
import kotlinx.android.synthetic.main.toolbar.*

import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.adapter.user.UsersListAdapter
import ru.vladislav_akulinin.mychat_version_2.adapter.user.OnItemClickedListener
import ru.vladislav_akulinin.mychat_version_2.model.User
import ru.vladislav_akulinin.mychat_version_2.ui.activity.MainActivity

class AddChatFragment : Fragment(), OnItemClickedListener {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

    private lateinit var usersListAdapter: UsersListAdapter
    private var userList = ArrayList<User>()

    var total_item = 0
    var last_visibe_item = 0
    var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_chat, container, false)

        val parentActivity : MainActivity = activity as MainActivity // parent reference
        parentActivity.setToolbar()
        parentActivity.toolbar.title = "Добавить чат"

        val layoutManager = LinearLayoutManager(context)
        view.recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(view.recycler_view.context, layoutManager.orientation)
        view.recycler_view.addItemDecoration(dividerItemDecoration)

        usersListAdapter = UsersListAdapter(context)
        usersListAdapter.registerOnItemCallBack(this)
        view.recycler_view.adapter = usersListAdapter

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

    override fun onLongClicked(user: User): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClicked(user: User) {
        createNewChat(user)
    }

    @SuppressLint("PrivateResource")
    private fun createNewChat(user: User){
        val intent = Intent().putExtra("userid", user.id)

        fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.container, MessageFragment(intent))
                .addToBackStack(null)
                .commit()
    }

    //для поиска пользователя
    private fun searchUser(search: String, view: View) {
        val query: Query = FirebaseDatabase.getInstance().reference
                .child(UsersFragment.USER_PATH_KEY)
                .orderByChild(UsersFragment.SEARCH_KEY)
                .startAt(search)
                .endAt(search + "\uf8ff")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                userList.clear()

                for (snapshot in p0.children) {
                    val user = snapshot.getValue(User::class.java)!!

                    if (user.id != firebaseUser?.uid) {
                        userList.add(user)
                    }
                }

                usersListAdapter = UsersListAdapter(context)
                view.recycler_view.adapter = usersListAdapter
                usersListAdapter.addAll(userList)
                isLoading = false
            }
        })

    }

    private fun getUsers() {
        val query: Query = FirebaseDatabase.getInstance().reference
                .child(UsersFragment.USER_PATH_KEY)
                .orderByKey()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                isLoading = if(p0.hasChildren()) {
                    for (snapshot in p0.children) {
                        val user = snapshot.getValue(User::class.java)!!

                        if (user.id != firebaseUser?.uid) {
                            userList.add(user)
                        }
                    }
                    usersListAdapter.addAll(userList)
                    false
                } else {
                    false
                }
            }
        })
    }
}
