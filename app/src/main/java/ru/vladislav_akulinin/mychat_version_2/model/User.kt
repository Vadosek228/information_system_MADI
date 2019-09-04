package ru.vladislav_akulinin.mychat_version_2.model

class User
//(
//        var id: String? = null,
//        var firstName: String? = null,
//        var lastName: String? = null,
//        var fatherName: String? = null,
//        var phoneNumber: Int? = null,
//        var email: String? = null,
//        var statusUser: String?=null, //1 - админ, 2 - студент, 3 - преподаватель
//        var imageURL: String? = null,
//        var status: String? = null,
//        var search: String? = null,
//        var about: String? = null,
//        var course: Int? = null,
//        var group: String? = null
//)
{

    var id: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var fatherName: String? = null
    var phoneNumber: Long? = null
    var email: String? = null
    var statusUser: String?=null //1 - админ, 2 - студент, 3 - преподаватель
    var imageURL: String? = null
    var status: String? = null
    var search: String? = null
    var about: String? = null
    var course: Long? = null
    var group: String? = null

//    constructor() :this("","", "","", 0, "", "", "", "", "","", 0,"" )
}