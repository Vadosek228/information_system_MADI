package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

interface ChatInterface {

    interface View {
        fun initViewChat()
        fun getChatList(userList: MutableList<UserModel>)
    }

    interface Presenter {
        fun getChatList(presenter: ChatPresenter)
        fun loadChatListData(userList: MutableList<UserModel>)
    }

    interface Model {
        fun getChatList(presenter: ChatPresenter)
    }
}