package ru.vladislav_akulinin.mychat_version_2.mvp.chat

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

interface ChatInterface {

    interface View {
        fun initViewChat()
        fun updateUserList(userList: MutableList<UserModel>)
    }

    interface Presenter {
        fun setUserList(presenter: ChatPresenter)
        fun getUserList(): MutableList<UserModel>?
        fun loadUserListData(userList: MutableList<UserModel>)
    }

    interface Model {
        fun getUserList(presenter: ChatPresenter): MutableList<UserModel>
    }
}