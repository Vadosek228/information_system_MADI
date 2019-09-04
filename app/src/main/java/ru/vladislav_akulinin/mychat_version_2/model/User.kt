package ru.vladislav_akulinin.mychat_version_2.model

data class User(
        var id: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var fatherName: String? = null,
        var phoneNumber: String? = null,
        var email: String? = null,
        var statusUser: String? = null,
        var imageURL: String? = null,
        var status: String? = null,
        var search: String? = null,
        var about: String? = null,
        var course: String? = null,
        var group: String? = null
){
    constructor()
            :this("","", "","", "", "", "", "", "", "","", "","" )
}