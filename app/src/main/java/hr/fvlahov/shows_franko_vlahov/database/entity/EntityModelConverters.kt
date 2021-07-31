package hr.fvlahov.shows_franko_vlahov.database.entity

interface ModelConverter<T> {
    fun convertToEntity(): T
}

interface EntityConverter<T> {
    fun convertToModel(): T
}