package ru.vladislav_akulinin.mychat_version_2.ui.mvp.user

import ru.vladislav_akulinin.mychat_version_2.model.User

class UserPresenter(_view: UserInterface.View): UserInterface.Presenter {
    private var view: UserInterface.View = _view
    private var model: UserInterface.Model = UserModel()

    private var userList: MutableList<User> ?= null

    init {
        view.initViewChat()
    }

    override fun getUserList(presenter: UserPresenter) {
        model.getUserList(this) //передали интерфейс презентора
    }

    override fun loadUserListData(loadUserList: MutableList<User>){
        userList = loadUserList
        view.updateUserList(userList!!)
    }
}