package top.defaults.kotlinoverflow.data.resp

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Question(

    @field:SerializedName("score")
	val score: Int? = null,

    @field:SerializedName("link")
	val link: String? = null,

    @field:SerializedName("share_link")
    val shareLink: String? = null,

    @field:SerializedName("last_activity_date")
	val lastActivityDate: Int? = null,

    @field:SerializedName("last_edit_date")
    val lastEditDate: Int? = null,

    @field:SerializedName("owner")
	val owner: ShallowUser? = null,

    @field:SerializedName("last_editor")
	val lastEditor: ShallowUser? = null,

    @field:SerializedName("is_answered")
	val isAnswered: Boolean? = null,

    @field:SerializedName("accepted_answer_id")
	val acceptedAnswerId: Int? = null,

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
	val tags: List<String?>? = null,

    @field:SerializedName("comment_count")
    val commentCount: Int? = null,

    @field:SerializedName("comments")
    val comments: List<Comment?>? = null,

    @field:SerializedName("answers")
    val answers: List<Answer?>? = null
) : Parcelable {
	@Suppress("unused")
	companion object {
		@JvmField val CREATOR: Parcelable.Creator<Question> = object : Parcelable.Creator<Question> {
			override fun createFromParcel(source: Parcel): Question = Question(source)
			override fun newArray(size: Int): Array<Question?> = arrayOfNulls(size)
		}
	}

	constructor(source: Parcel) : this(
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readString(),
	source.readString(),
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readParcelable<ShallowUser>(ShallowUser::class.java.classLoader),
	source.readParcelable<ShallowUser>(ShallowUser::class.java.classLoader),
	source.readValue(Boolean::class.java.classLoader) as Boolean?,
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readString(),
	source.readString(),
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?,
	ArrayList<String?>().apply { source.readList(this, String::class.java.classLoader) },
	source.readValue(Int::class.java.classLoader) as Int?,
	ArrayList<Comment?>().apply { source.readList(this, Comment::class.java.classLoader) },
	ArrayList<Answer?>().apply { source.readList(this, Answer::class.java.classLoader) }
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeValue(score)
		dest.writeString(link)
		dest.writeString(shareLink)
		dest.writeValue(lastActivityDate)
		dest.writeValue(lastEditDate)
		dest.writeParcelable(owner, 0)
		dest.writeParcelable(lastEditor, 0)
		dest.writeValue(isAnswered)
		dest.writeValue(acceptedAnswerId)
		dest.writeValue(creationDate)
		dest.writeValue(upVoteCount)
		dest.writeValue(answerCount)
		dest.writeString(title)
		dest.writeString(body)
		dest.writeValue(questionId)
		dest.writeValue(viewCount)
		dest.writeList(tags)
		dest.writeValue(commentCount)
		dest.writeList(comments)
		dest.writeList(answers)
	}
}