package top.defaults.kotlinoverflow.data.resp

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
	companion object {
		@JvmField val CREATOR: Parcelable.Creator<ShallowUser> = object : Parcelable.Creator<ShallowUser> {
			override fun createFromParcel(source: Parcel): ShallowUser = ShallowUser(source)
			override fun newArray(size: Int): Array<ShallowUser?> = arrayOfNulls(size)
		}
	}

	constructor(source: Parcel) : this(
	source.readString(),
	source.readString(),
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readString(),
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readParcelable<BadgeCounts>(BadgeCounts::class.java.classLoader),
	source.readString()
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeString(profileImage)
		dest.writeString(userType)
		dest.writeValue(userId)
		dest.writeString(link)
		dest.writeValue(reputation)
		dest.writeParcelable(badgeCounts, 0)
		dest.writeString(displayName)
	}
}