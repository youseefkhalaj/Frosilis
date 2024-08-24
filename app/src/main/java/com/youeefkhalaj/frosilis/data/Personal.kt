package com.youeefkhalaj.frosilis.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personals")
data class Personal(
@PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val image: Bitmap?,
    val currentMonthOverTime: Int,
    val overTimeAddCurrentMonth: Int,
    var horseOvertime: Array<Array<IntArray>>,
    val shift: Shift
)





