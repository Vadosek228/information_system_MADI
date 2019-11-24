package ru.vladislav_akulinin.mychat_version_2.model

data class News(
        var subject: String? = null,
        var text: String? = null
) {
    constructor()
            :this("", "")
}