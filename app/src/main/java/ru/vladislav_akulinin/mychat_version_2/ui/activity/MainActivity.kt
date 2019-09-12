package ru.vladislav_akulinin.mychat_version_2.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.nav_header_main.*
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.UserModel
import ru.vladislav_akulinin.mychat_version_2.chat.ChatsFragment
import ru.vladislav_akulinin.mychat_version_2.ui.fragments.*


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    private var container: FrameLayout? = null

    internal lateinit var profile_image: CircleImageView
    internal lateinit var username: TextView

    private var firebaseUser = FirebaseAuth.getInstance().currentUser
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference
            .child("UserNew")
            .child(firebaseUser!!.uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)
        setToolbar()
        configureDrawerLayout()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.colorAccent, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorAccent)
        }

        drawerLayout = findViewById(R.id.drawerlayout)

        container = findViewById(R.id.container)

        val fragment = UsersFragment()
        addFragment(fragment)

        getUserData()
    }

    fun setToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureDrawerLayout() {
        drawerLayout = findViewById(R.id.drawerlayout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setNavigateView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStatus(status: String) {
        firebaseDatabase
                .child("status")
                .setValue(status)
    }

    private fun getUserData(){
        firebaseDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val myProfile = dataSnapshot.getValue(UserModel::class.java)
                tv_username_nav_header.text = myProfile?.firstName + " " + myProfile?.lastName
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setStatus("online")
    }

    override fun onPause() {
        super.onPause()
        setStatus("offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        setStatus("offline")
    }

    private fun setNavigateView() {
        val navigationView = findViewById<NavigationView>(R.id.navigation)

        navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_contacts -> {
                    val fragment = UsersFragment()
                    addFragment(fragment)
                    toolbar.setTitle(R.string.menu_contacts)
                }
                R.id.nav_message -> {
                    val fragment = ChatsFragment()
                    addFragment(fragment)
                    toolbar.setTitle(R.string.menu_chats)
                }
                R.id.nav_load -> {
                    val fragment = LoadFragment()
                    addFragment(fragment)
                    toolbar.setTitle(R.string.menu_load)
                }
                R.id.nav_library -> {
                    val fragment = LibraryFragment()
                    addFragment(fragment)
                    toolbar.setTitle(R.string.menu_library)
                }
                R.id.nav_profile -> {
                    val fragment = ProfileFragment()
                    addFragment(fragment)
                    toolbar.setTitle(R.string.menu_profile)
                }
                R.id.nav_exit -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@MainActivity, StartActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            drawerLayout.closeDrawers()
            true
        }
    }

    @SuppressLint("PrivateResource")
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }
}