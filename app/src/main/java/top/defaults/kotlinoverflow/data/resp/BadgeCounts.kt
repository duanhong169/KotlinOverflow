package top.defaults.kotlinoverflow.data.resp

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BadgeCounts(

	@field:SerializedName("gold")
	val gold: Int? = null,

	@field:SerializedName("silver")
	val silver: Int? = null,

	@field:SerializedName("bronze")
	val bronze: Int? = null
) : Parcelable {
	companion object {
		@JvmField val CREATOR: Parcelable.Creator<BadgeCounts> = object : Parcelable.Creator<BadgeCounts> {
			override fun createFromParcel(source: Parcel): BadgeCounts = BadgeCounts(source)
			override fun newArray(size: Int): Array<BadgeCounts?> = arrayOfNulls(size)
		}
	}

	constructor(source: Parcel) : this(
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?,
	source.readValue(Int::class.java.classLoader) as Int?
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) {
		dest.writeValue(gold)
		dest.writeValue(silver)
		dest.writeValue(bronze)
	}
}