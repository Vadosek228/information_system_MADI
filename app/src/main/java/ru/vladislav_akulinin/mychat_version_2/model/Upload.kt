package ru.vladislav_akulinin.mychat_version_2.model

data class Upload(
        var name: String,
        var url: String
) {
    constructor() : this("","")
}