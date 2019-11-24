package ru.vladislav_akulinin.mychat_version_2.ui.mvp.news

import ru.vladislav_akulinin.mychat_version_2.model.News

interface NewsInterface {

    interface View {
        fun initViewNews()
        fun updateNewsList(newsList: MutableList<News>)
    }

    interface Presenter {
        fun getNewsList(presenter: NewsPresenter)
        fun loadNewsListData(newsList: MutableList<News>)
    }

    interface Model {
        fun getNewsList(presenter: NewsPresenter): MutableList<News>
    }
}