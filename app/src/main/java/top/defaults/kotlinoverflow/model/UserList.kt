package top.defaults.kotlinoverflow.model

import com.google.gson.annotations.SerializedName

data class UserList(

	@field:SerializedName("quota_max")
	val quotaMax: Int? = null,

	@field:SerializedName("quota_remaining")
	val quotaRemaining: Int? = null,

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null,

	@field:SerializedName("items")
	val items: List<User?>? = null
)