package top.defaults.kotlinoverflow.model

import com.google.gson.annotations.SerializedName

data class Question(

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("last_activity_date")
	val lastActivityDate: Int? = null,

	@field:SerializedName("owner")
	val owner: ShallowUser? = null,

	@field:SerializedName("is_answered")
	val isAnswered: Boolean? = null,

	@field:SerializedName("creation_date")
	val creationDate: Int? = null,

	@field:SerializedName("up_vote_count")
	val upVoteCount: Int? = null,

	@field:SerializedName("answer_count")
	val answerCount: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("question_id")
	val questionId: Int? = null,

	@field:SerializedName("view_count")
	val viewCount: Int? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null
)