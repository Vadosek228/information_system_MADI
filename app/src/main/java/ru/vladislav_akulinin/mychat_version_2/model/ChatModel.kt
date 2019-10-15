package ru.vladislav_akulinin.mychat_version_2.model

data class ChatModel(
        var id: String ?= null,
        var idSendUser: String ?= null,
        var idReadUser: String ?= null,
        var userName: String ?= null,
        var userImage: String ?= null,
        var userMessage: String ?= null,
        var userStatus: String ?= null
){
    constructor()
            :this("","", "","", "" )
}