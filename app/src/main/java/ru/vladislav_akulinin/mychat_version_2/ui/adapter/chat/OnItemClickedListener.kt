package ru.vladislav_akulinin.mychat_version_2.ui.adapter.chat

import ru.vladislav_akulinin.mychat_version_2.model.Chat

interface OnItemClickedListener {

    fun onClicked(userModel: Chat)

    fun onLongClicked(userModel: Chat): Boolean
}