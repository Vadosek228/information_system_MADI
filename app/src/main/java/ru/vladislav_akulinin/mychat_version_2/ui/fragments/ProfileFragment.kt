package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.graphics.ColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile_new.*

import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.Profile
import ru.vladislav_akulinin.mychat_version_2.model.User
import ru.vladislav_akulinin.mychat_version_2.model.UserJava
import ru.vladislav_akulinin.mychat_version_2.utils.Utils
import ru.vladislav_akulinin.mychat_version_2.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    internal var reference: DatabaseReference? = null
    internal var fuser: FirebaseUser? = null

    private lateinit var viewModel : ProfileViewModel
    var isEditMode = false
    lateinit var viewFields :Map<String, TextView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile_new, container, false)

        fuser = FirebaseAuth.getInstance().currentUser
        reference = fuser?.uid?.let { FirebaseDatabase.getInstance().getReference("UserNew").child(it) }

        reference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserJava::class.java)
                tv_nick_name.text = user?.username
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return view
    }
}

//class ProfileFragment : Fragment() {
//
//    companion object {
//        fun newInstance(): ProfileFragment = ProfileFragment()
//        const val IS_EDIT_MODE = "IS_EDIT_MODE"
//    }
//
//    internal var reference: DatabaseReference? = null
//    internal var fuser: FirebaseUser? = null
//
//    private lateinit var viewModel : ProfileViewModel
//    var isEditMode = false
//    lateinit var viewFields :Map<String, TextView>
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//
//        val view = inflater.inflate(R.layout.fragment_profile_new, container, false)
//
//        fuser = FirebaseAuth.getInstance().currentUser
//        reference = fuser?.uid?.let { FirebaseDatabase.getInstance().getReference("User").child(it) }
//
//        reference?.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user = dataSnapshot.getValue(UserJava::class.java)
//                tv_nick_name.text = user?.username
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
//
////        initViews(savedInstanceState)
////        initViewModel()
//
//        return view
//    }


//    private fun initViewModel(){
//        //получаем viewModel и передаем текущую Activity
//        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java) //класс, который хотим получить
//
//        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
//
//        viewModel.getRepositoryError().observe(this, Observer { updateRepoError(it) })
//    }
//
//    private fun updateRepoError(isError: Boolean) {
//        wr_repository.isErrorEnabled = isError
//        wr_repository.error = if (isError) "Невалидный адрес репозитория" else null
//    }
//
//    private fun updateRepository(isError: Boolean) {
//        if (isError) et_repository.text.clear()
//    }

//    //связывает даные из viewModel
//    fun initViews(saveInstanceState : Bundle?){
//        viewFields = mapOf(
//                "nickName" to tv_nick_name,
//                "rank" to tv_rank,
//                "firstName" to et_first_name,
//                "lastName" to et_last_name,
//                "about" to et_about,
//                "repository" to et_repository,
//                "rating" to tv_rating,
//                "respect" to tv_respect
//        )
//
//        isEditMode = saveInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
//        showCurrentMode(isEditMode)
//
//        btn_edit.setOnClickListener{
//            viewModel.onRepoEditCompleted(wr_repository.isErrorEnabled)
//
//            if(isEditMode) saveProfileInfo()
//            isEditMode = !isEditMode
//            showCurrentMode(isEditMode)
//        }
//
//        btn_switch_theme.setOnClickListener{
//            viewModel.switchTheme()
//        }
//
//        et_repository.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
//            override fun afterTextChanged(s: Editable?) {
//                viewModel.onRepoChanged(s.toString())
//            }
//        })
//    }

//    private fun showCurrentMode(isEdit: Boolean) {
//        val info = viewFields.filter { setOf("firstName", "lastName" , "about", "repository").contains(it.key) }
//
//        for((_,v) in info){ //пробегаемся по мапе (нижнее подчеркивание - игнорировать ключи)
//            v as EditText
//            v.isFocusable = isEdit
//            v.isFocusableInTouchMode = isEdit
//            v.isEnabled = isEdit
//            v.background.alpha = if(isEdit) 255 else 0
//        }
//
//        ic_eye.visibility = if(isEdit) View.GONE else View.VISIBLE
//        wr_about.isCounterEnabled = isEdit //показываем колличество символов
//
//        with(btn_edit) {
//            val filter: ColorFilter? = if(isEdit){
//                android.graphics.PorterDuffColorFilter(
//                        resources.getColor(R.color.color_accent),
//                        android.graphics.PorterDuff.Mode.SRC_IN //принимает режим наложения
//                )
//            } else { // если не режим редактирования
//                null
//            }
//
//            val icon = if (isEdit) {
//                resources.getDrawable(R.drawable.ic_save_black_24dp)
//            } else {
//                resources.getDrawable(R.drawable.ic_edit_black_24dp)
//            }
//
//            background.colorFilter = filter
//            setImageDrawable(icon)
//        }
//
//    }

//    private fun updateUI(profile: Profile) {
//        profile.toMap().also {
//            for ((k, v) in viewFields) {
//                v.text = it[k].toString()
//            }
//        }
//
//        val initials = Utils.toInitials(profile.firstName, profile.lastName)
//        iv_avatar.updateAvatar(initials)
//    }
//
//    private fun saveProfileInfo() {
//        Profile(
//                firstName = et_first_name.text.toString(),
//                lastName = et_last_name.text.toString(),
//                about = et_about.text.toString(),
//                repository = et_repository.text.toString()
//        ).apply {
//            viewModel.saveProfileData(this)
//        }
//    }
//}