package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.os.Bundle
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
import ru.vladislav_akulinin.mychat_version_2.model.UserList


class UsersFragment : Fragment() {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

    private lateinit var userAdapter: UserAdapter

    var total_item = 0
    var last_visibe_item = 0

    var isLoading = false


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


//        //поле поиска пользователя
//        search_user = view.findViewById(R.id.search_user)
//        search_user.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
//                searchUser(charSequence.toString().toLowerCase())
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//            }
//        })

        return view
    }

//    //для поиска пользователя
//    private fun searchUser(s: String) {
//
//        val fuser = FirebaseAuth.getInstance().currentUser
//        val query = FirebaseDatabase.getInstance().getReference("UserNew").orderByChild("search")
//                .startAt(s)
//                .endAt(s + "\uf8ff")
//
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                mUser?.clear()
//                for (snapshot in dataSnapshot.children) {
//                    val user = snapshot.getValue(User::class.java)!!
//
//                    assert(fuser != null)
//                    if (user.id != fuser!!.uid) {
//                        mUser?.add(user)
//                    }
//                }
//
//                userAdapter = UserAdapter(context, mUser, false)
//                recycler_view.adapter = userAdapter //установить новое значение в ресайкл из поиска
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
//
//    }

    private fun getUsers() {
        val query: Query = FirebaseDatabase.getInstance().reference
                .child("UserNew")
                .orderByKey()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    val userList = ArrayList<UserList>()

                    for (snapshot in p0.children) {
                        val user = snapshot.getValue(UserList::class.java)!!

                        if (user.id != firebaseUser?.uid) {
                            userList.add(user)
                        }
                    }

                    userAdapter.addAll(userList)
                    isLoading = false
                } else {
                    isLoading = false
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