package c14220270.paba.tasklist

import android.os.Parcel
import android.os.Parcelable

data class taskList(
    var name : String,
    var description : String,
    var date : String,
    var status : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<taskList> {
        override fun createFromParcel(parcel: Parcel): taskList {
            return taskList(parcel)
        }

        override fun newArray(size: Int): Array<taskList?> {
            return arrayOfNulls(size)
        }
    }
}
