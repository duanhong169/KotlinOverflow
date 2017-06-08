package top.defaults.kotlinoverflow.model

import com.google.gson.annotations.SerializedName

data class ShallowUser(

	@field:SerializedName("profile_image")
	val profileImage: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("reputation")
	val reputation: Int? = null,

	@field:SerializedName("badge_counts")
	val badgeCounts: BadgeCounts? = null,

	@field:SerializedName("display_name")
	val displayName: String? = null
)