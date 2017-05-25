package top.defaults.kotlinoverflow.util

val DEF_PAGE_SIZE = 30

class Paging(var page: Int = 1, val pageSize: Int = DEF_PAGE_SIZE) {

    fun inc() {
        page++
    }

    fun reset() {
        page = 1
    }
}