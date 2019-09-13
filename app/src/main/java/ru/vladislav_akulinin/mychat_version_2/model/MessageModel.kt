package ru.vladislav_akulinin.mychat_version_2.model

data class MessageModel(
        val sender: String? = null,
        val receiver: String? = null,
        val message: String? = null,
        val isseen: Boolean? = null
//        val author: String? = null,
//        val time: String? = null
) {
    constructor() : this("", "", "", null)
}