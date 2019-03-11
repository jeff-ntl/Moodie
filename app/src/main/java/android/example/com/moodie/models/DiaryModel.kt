package android.example.com.moodie.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiaryModel(var title: String = "",
                      var description: String = ""): Parcelable