package top.defaults.kotlinoverflow.data.resp

import com.google.gson.annotations.SerializedName

data class Comment(

    @field:SerializedName("owner")
	val owner: ShallowUser? = null,

    @field:SerializedName("score")
	val score: Int? = null,

    @field:SerializedName("post_id")
	val postId: Int? = null,

    @field:SerializedName("edited")
	val edited: Boolean? = null,

    @field:SerializedName("creation_date")
	val creationDate: Int? = null,

    @field:SerializedName("comment_id")
	val commentId: Int? = null,

    @field:SerializedName("body")
	val body: String? = null
)