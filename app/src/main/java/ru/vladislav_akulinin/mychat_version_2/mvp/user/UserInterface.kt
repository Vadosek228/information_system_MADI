package ru.vladislav_akulinin.mychat_version_2.mvp.user

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

interface UserInterface {

    interface View {
        fun initViewChat()
        fun updateUserList(userList: MutableList<UserModel>)
    }

    interface Presenter {
        fun getUserList(presenter: UserPresenter)
        fun loadUserListData(userList: MutableList<UserModel>)
    }

    interface Model {
        fun getUserList(presenter: UserPresenter): MutableList<UserModel>
    }
}