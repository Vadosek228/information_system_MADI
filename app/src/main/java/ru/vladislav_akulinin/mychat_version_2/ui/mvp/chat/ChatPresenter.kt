package ru.vladislav_akulinin.mychat_version_2.ui.mvp.chat

import ru.vladislav_akulinin.mychat_version_2.model.Chat

class ChatPresenter(_view: ChatInterface.View): ChatInterface.Presenter {
    private var view: ChatInterface.View = _view
    private var model: ChatInterface.Model = ChatModel()

    private var userList: MutableList<Chat> ?= null

    init {
        view.initViewChat()
    }

    override fun getChatList(presenter: ChatPresenter) {
        model.getChatList(this)
    }

    override fun loadChatListData(loadChatList: MutableList<Chat>){
        userList = loadChatList
        view.getChatList(userList!!)
    }
}