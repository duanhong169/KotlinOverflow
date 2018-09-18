package top.defaults.kotlinoverflow.data.param

import top.defaults.kotlinoverflow.util.DEF_PAGE_SIZE

data class QuestionsQueryParams(val page: Int = 1,
                                val pageSize: Int = DEF_PAGE_SIZE,
                                val tagged: String = "kotlin",
                                val order: String = "desc",
                                val sort: String = "activity",
                                val filter: String = "!6JW7LsM8VJgxM")