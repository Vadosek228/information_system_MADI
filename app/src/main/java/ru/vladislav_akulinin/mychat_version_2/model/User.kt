package ru.vladislav_akulinin.mychat_version_2.model

class User(
        var id: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var imageURL: String? = null,
        var status: String? = null,
        var search: String? = null,
        var about : String,
        var course : Int = 0,
        var group : String? = null
) {
    fun User(id: String, firstName: String?, lastName: String?, imageURL: String, status: String, search: String, about: String, course: Int, group: String) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.imageURL = imageURL
        this.status = status
        this.search = search
        this.about = about
        this.course = course
        this.group = group

        fun User() {

        }

        fun getId(): String? {
            return id
        }

        fun setId(id: String) {
            this.id = id
        }

        fun getFirstName(): String? {
            return firstName
        }

        fun setFirstName(firstName: String) {
            this.firstName = firstName
        }

        fun getLastName(): String? {
            return lastName
        }

        fun setLastName(lastName: String) {
            this.lastName = lastName
        }

        fun getImageURL(): String? {
            return imageURL
        }

        fun setImageURL(imageURL: String) {
            this.imageURL = imageURL
        }

        fun getStatus(): String? {
            return status
        }

        fun setStatus(status: String) {
            this.status = status
        }

        fun getSearch(): String? {
            return search
        }

        fun setSearch(search: String) {
            this.search = search
        }

        fun getAbout(): String? {
            return about
        }

        fun setAbout(about: String) {
            this.about = about
        }

        fun getCourse(): Int? {
            return course
        }

        fun setCourse(rating: Int) {
            this.course = course
        }

        fun getGroup(): String? {
            return group
        }

        fun setGroup(group: String) {
            this.group = group
        }
    }
}