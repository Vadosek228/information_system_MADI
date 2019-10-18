package ru.vladislav_akulinin.mychat_version_2.mvp.user

import ru.vladislav_akulinin.mychat_version_2.model.User

interface UserInterface {

    interface View {
        fun initViewChat()
        fun updateUserList(userList: MutableList<User>)
    }

    interface Presenter {
        fun getUserList(presenter: UserPresenter)
        fun loadUserListData(userList: MutableList<User>)
    }

    interface Model {
        fun getUserList(presenter: UserPresenter): MutableList<User>
    }
}