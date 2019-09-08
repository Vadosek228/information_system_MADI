package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile_new.view.*
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.User
import ru.vladislav_akulinin.mychat_version_2.utils.Utils

class ProfileFragment : Fragment() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val firebaseMyDatabase: Query = FirebaseDatabase.getInstance().reference
            .child("UserNew")
            .child(firebaseUser!!.uid)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_new, container, false)

        uploadYourData(view)

        return view
    }

    private fun uploadYourData(view:View){
        firebaseMyDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myProfile = dataSnapshot.getValue(User::class.java)

                view.tv_nick_name.text = myProfile?.firstName + " " + myProfile?.lastName
                view.tv_rank.text = myProfile?.status //User
                view.et_last_name.setText(myProfile?.lastName)
                view.et_first_name.setText(myProfile?.firstName)
                view.iv_avatar.updateAvatar(Utils.toInitials(myProfile?.firstName, myProfile?.lastName))
            }
        })
    }
}