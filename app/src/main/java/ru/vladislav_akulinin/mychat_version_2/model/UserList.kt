package ru.vladislav_akulinin.mychat_version_2.model

class UserList {
//    var uid: String? = null
//    var firstName: String? = null
//    var lastName: String? = null
//    var userStatus: String? = null
//
//
//    constructor() {}  // Needed for Firebase
//
//    constructor(uid: String, firstName: String, lastName: String, userStatus: String) {
//        this.uid = uid
//        this.firstName = firstName
//        this.lastName = lastName
//        this.userStatus = userStatus
//    }

    var id: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var fatherName: String? = null
    var email: String? = null
    var statusUser: String?=null //1 - админ, 2 - студент, 3 - преподаватель
    var imageURL: String? = null
}