package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import ru.vladislav_akulinin.mychat_version_2.model.Chat

interface ChatInterface {

    interface View {
        fun initViewChat()
        fun getChatList(userList: MutableList<Chat>)
    }

    interface Presenter {
        fun getChatList(presenter: ChatPresenter)
        fun loadChatListData(userList: MutableList<Chat>)
    }

    interface Model {
        fun getChatList(presenter: ChatPresenter)
    }
}