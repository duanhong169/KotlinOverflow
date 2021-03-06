package top.defaults.kotlinoverflow.util

const val DEF_PAGE_SIZE = 30

data class Paging(var page: Int = 1, val pageSize: Int = DEF_PAGE_SIZE) {

    var hasMore: Boolean = true

    fun inc() {
        page++
    }

    fun reset() {
        page = 1
        hasMore = true
    }
}