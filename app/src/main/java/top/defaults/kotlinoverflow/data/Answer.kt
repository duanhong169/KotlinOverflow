package top.defaults.kotlinoverflow.data

import com.google.gson.annotations.SerializedName

data class Answer(

    @field:SerializedName("comment_count")
	val commentCount: Int? = null,

    @field:SerializedName("share_link")
	val shareLink: String? = null,

    @field:SerializedName("score")
	val score: Int? = null,

    @field:SerializedName("is_accepted")
	val isAccepted: Boolean? = null,

    @field:SerializedName("last_activity_date")
	val lastActivityDate: Int? = null,

    @field:SerializedName("owner")
	val owner: ShallowUser? = null,

    @field:SerializedName("last_editor")
	val lastEditor: ShallowUser? = null,

    @field:SerializedName("creation_date")
	val creationDate: Int? = null,

    @field:SerializedName("body")
	val body: String? = null,

    @field:SerializedName("answer_id")
	val answerId: Int? = null,

    @field:SerializedName("question_id")
	val questionId: Int? = null
)