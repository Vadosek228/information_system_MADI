package ru.vladislav_akulinin.mychat_version_2.utils


val symbolMap = mapOf("а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d", "е" to "e", "ё" to "e", "ж" to "zh",
        "з" to "z", "и" to "i", "й" to "i", "к" to "k", "л" to "l", "м" to "m", "н" to "n", "о" to "o", "п" to "p",
        "р" to "r", "с" to "s", "т" to "t", "у" to "u", "ф" to "f", "х" to "h", "ц" to "c", "ч" to "ch", "ш" to "sh",
        "щ" to "sh", "ъ" to "", "ы" to "i", "ь" to "", "э" to "e", "ю" to "yu", "я" to "ya")

object Utils {

    //разбиение одной строки на несколько
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName.isNullOrBlank()) firstName = null
        if (lastName.isNullOrBlank()) lastName = null

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {

        return buildString {
            for (char in payload) {
                val transSymbols = symbolMap[char.toLowerCase().toString()]
                when {
                    char == ' ' -> append(divider)
                    char.isUpperCase() -> {
                        if (transSymbols != null && transSymbols.length > 1) {
                            val firstSymbol = transSymbols.substring(0, 1)
                            append(transSymbols.replace(firstSymbol, firstSymbol.toUpperCase()))
                        } else {
                            append(transSymbols?.toUpperCase() ?: char)
                        }
                    }
                    else -> append(transSymbols ?: char)
                }
            }
        }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val first: String = (firstName?.trim()?.firstOrNull() ?: "").toString()
        val last: String = (lastName?.trim()?.firstOrNull() ?: "").toString()

        if (first == "" && last == "") return null
        return "$first$last".toUpperCase()
    }

//    fun sendMessage(benderObj : Bender, messageEt : EditText?, benderImage : ImageView?, textTxt : TextView?){
//        val (phrase, color) = benderObj.listenAnswer(messageEt?.text.toString())//.toLowerCase()) //приводим к нижнему регистору
//        messageEt?.setText("")
//        val (r,g,b) = color
//        benderImage?.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
//        textTxt?.text = phrase
//    }
}