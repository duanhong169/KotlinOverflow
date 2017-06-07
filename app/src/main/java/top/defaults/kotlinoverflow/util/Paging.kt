package top.defaults.kotlinoverflow.util

val DEF_PAGE_SIZE = 30

data class Paging(var page: Int = 1, val pageSize: Int = DEF_PAGE_SIZE) {

    var hasNext: Boolean = true

    fun inc() {
        page++
    }

    fun reset() {
        page = 1
        hasNext = true
    }
}