package ru.vladislav_akulinin.mychat_version_2.ui.mvp.news

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.vladislav_akulinin.mychat_version_2.model.News

class NewsModel : NewsInterface.Model {
    private lateinit var newsList: MutableList<News>
    var firebaseReference = FirebaseDatabase.getInstance().reference

    override fun getNewsList(presenter: NewsPresenter): MutableList<News> {
        newsList = ArrayList()
        val firebaseDatabaseUser = firebaseReference.child("News")
        firebaseDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                newsList.clear()
                for (snapshot in dataSnapshot.children) {
                    val news = snapshot.getValue(News::class.java)
                    if (news != null) {
                        newsList.add(news)
                    }
                }
                presenter.loadNewsListData(newsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

        return newsList
    }
}