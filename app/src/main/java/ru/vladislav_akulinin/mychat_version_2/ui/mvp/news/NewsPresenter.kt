package ru.vladislav_akulinin.mychat_version_2.ui.mvp.news

import ru.vladislav_akulinin.mychat_version_2.model.News

class NewsPresenter(_view: NewsInterface.View): NewsInterface.Presenter  {

    private var view: NewsInterface.View = _view
    private var model: NewsInterface.Model = NewsModel()

    private var newsList: MutableList<News> ?= null

    init {
        view.initViewNews()
    }

    override fun getNewsList(presenter: NewsPresenter) {
        model.getNewsList(this)
    }

    override fun loadNewsListData(loadNewsList: MutableList<News>){
        newsList = loadNewsList
        view.updateNewsList(newsList!!)
    }
}