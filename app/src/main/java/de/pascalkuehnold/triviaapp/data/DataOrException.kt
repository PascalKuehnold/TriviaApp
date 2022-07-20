package de.pascalkuehnold.triviaapp.data


/**
 * Created by Pascal Kühnold on 7/20/2022.
 */
data class DataOrException<T, Boolean, E: Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null
)

