package hr.fvlahov.shows_franko_vlahov.database.entity

interface ModelEntity <T> {
    fun convertToEntity() : T
}