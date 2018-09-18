package top.defaults.kotlinoverflow.data.param

import top.defaults.kotlinoverflow.`object`.AccessToken

data class UserQueryParams(val accessToken: String = AccessToken.value)