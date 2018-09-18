package top.defaults.kotlinoverflow.data.param

import top.defaults.kotlinoverflow.util.DEF_PAGE_SIZE

data class UsersQueryParams(val page: Int = 1,
                            val pageSize: Int = DEF_PAGE_SIZE,
                            val order: String = "desc",
                            val sort: String = "reputation")