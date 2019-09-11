package ru.vladislav_akulinin.mychat_version_2.model

data class ChatModel(
        val uid: String? = null,
        val author: String? = null,
        val message: String? = null,
        val time: String? = null
) {
    constructor() : this("", "", "")
}