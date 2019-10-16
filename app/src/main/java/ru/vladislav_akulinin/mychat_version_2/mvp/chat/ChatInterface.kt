package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

interface ChatInterface {

    interface View {
        fun initViewChat()
        fun updateUserList(userList: MutableList<UserModel>)
    }

    interface Presenter {
        fun getUserList(presenter: ChatPresenter)
        fun loadUserListData(userList: MutableList<UserModel>)
    }

    interface Model {
        fun getUserList(presenter: ChatPresenter): MutableList<UserModel>
    }
}