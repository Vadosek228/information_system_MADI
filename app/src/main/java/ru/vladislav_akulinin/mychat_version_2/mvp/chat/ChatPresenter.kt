package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

class ChatPresenter(_view: ChatInterface.View): ChatInterface.Presenter {
    private var view: ChatInterface.View = _view
    private var model: ChatInterface.Model = ChatModel()

    private var userList: MutableList<UserModel> ?= null

    init {
        view.initViewChat()
    }

    override fun setUserList(presenter: ChatPresenter) {
        model.getUserList(this)
    }

    override fun loadUserListData(loadUserList: MutableList<UserModel>){
        userList = loadUserList
        view.updateUserList(userList!!)
    }

    override fun getUserList() : MutableList<UserModel>? {
        return userList
    }
}