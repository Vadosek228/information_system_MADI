package ru.vladislav_akulinin.mychat_version_2.ui.adapter.user

import ru.vladislav_akulinin.mychat_version_2.model.User

interface OnItemClickedListener {

    fun onClicked(user: User)

    fun onLongClicked(user: User): Boolean
}