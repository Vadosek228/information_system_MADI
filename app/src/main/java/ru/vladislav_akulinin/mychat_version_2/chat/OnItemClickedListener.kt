package ru.vladislav_akulinin.mychat_version_2.chat

import ru.vladislav_akulinin.mychat_version_2.model.UserModel

interface OnItemClickedListener {

    fun onClicked(userModel: UserModel)

    fun onLongClicked(userModel: UserModel): Boolean
}