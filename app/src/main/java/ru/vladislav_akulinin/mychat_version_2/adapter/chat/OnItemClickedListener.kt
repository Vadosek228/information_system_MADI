package ru.vladislav_akulinin.mychat_version_2.adapter.chat

import ru.vladislav_akulinin.mychat_version_2.model.ChatModel

interface OnItemClickedListener {

    fun onClicked(userModel: ChatModel)

    fun onLongClicked(userModel: ChatModel): Boolean
}