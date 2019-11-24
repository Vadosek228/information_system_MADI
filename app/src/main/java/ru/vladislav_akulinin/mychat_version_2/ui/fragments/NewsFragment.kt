package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.android.synthetic.main.toolbar.*

import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.News
import ru.vladislav_akulinin.mychat_version_2.ui.activity.MainActivity
import ru.vladislav_akulinin.mychat_version_2.ui.adapter.news.NewsAdapter
import ru.vladislav_akulinin.mychat_version_2.ui.mvp.news.NewsInterface
import ru.vladislav_akulinin.mychat_version_2.ui.mvp.news.NewsPresenter

class NewsFragment : Fragment() , NewsInterface.View{
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    var firebaseReference = FirebaseDatabase.getInstance().reference.child("News")

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var yourNewsList: ArrayList<News>

    private var presenterNews: NewsPresenter ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        val parentActivity : MainActivity = activity as MainActivity
        parentActivity.setToolbar()
        parentActivity.toolbar.setTitle(R.string.menu_news)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        view.recycler_view_news.layoutManager = layoutManager

        yourNewsList = ArrayList()

        newsAdapter = NewsAdapter(context)
//        newsAdapter.registerOnItemCallBack(this)
        view.recycler_view_news.adapter = newsAdapter

        presenterNews = NewsPresenter(this)
        presenterNews?.let {
            it.getNewsList(it)
        }

        //todo create news
        view.floating_button_add_new_news.setOnClickListener{
            val news = News("tema 2", "text 2")
            firebaseReference.push().setValue(news)
        }

        return view
    }

    override fun initViewNews() {
        presenterNews?.getNewsList(presenterNews!!)
    }

    override fun updateNewsList(newsList: MutableList<News>) {
        newsAdapter.addAll(newsList)
    }
}
