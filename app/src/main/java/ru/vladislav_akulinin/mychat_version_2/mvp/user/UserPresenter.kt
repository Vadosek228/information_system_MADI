package ru.vladislav_akulinin.mychat_version_2.mvp.user

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

class UserPresenter(_view: UserInterface.View): UserInterface.Presenter {
    private var view: UserInterface.View = _view
    private var model: UserInterface.Model = ru.vladislav_akulinin.mychat_version_2.mvp.user.UserModel()

    private var userList: MutableList<UserModel> ?= null

    init {
        view.initViewChat()
    }

    override fun getUserList(presenter: UserPresenter) {
        model.getUserList(this) //передали интерфейс презентора
    }

    override fun loadUserListData(loadUserList: MutableList<UserModel>){
        userList = loadUserList
        view.updateUserList(userList!!)
    }
}