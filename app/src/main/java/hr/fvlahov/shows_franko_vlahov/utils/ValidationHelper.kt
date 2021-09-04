package hr.fvlahov.shows_franko_vlahov.utils

class ValidationHelper {
    //Regular expression for email validation -> any word + '@' + any word + match between 2 and 4 of the preceeding token
    private val emailRegex: Regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

    fun validateEmail(target: String): Boolean =
        emailRegex.matches(target)
}