package ru.vladislav_akulinin.mychat_version_2.mvp.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.vladislav_akulinin.mychat_version_2.model.UserModel

class UserModel : UserInterface.Model {
    private val mUser = FirebaseAuth.getInstance().currentUser
    private lateinit var userList: MutableList<UserModel>

    override fun getUserList(presenter: UserPresenter): MutableList<UserModel> {
        userList = ArrayList()
        val firebaseDatabaseUser = FirebaseDatabase.getInstance().reference.child("UserNew")
        firebaseDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null && mUser?.uid != user.id) {
                        userList.add(user)
                    }
                }
                presenter.loadUserListData(userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

        return userList
    }
}