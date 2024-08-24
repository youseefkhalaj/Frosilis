package com.youeefkhalaj.frosilis.data.calendarData.pojo

data class CalendarModel(
    val iranianDay: Int,
    val iranianMonth: Int,
    val iranianYear: Int,
    val dayOfWeek: Int,
    var gDay: Int,
    val gMonth: Int,
    val gYear: Int,
    val shift: String = "ص",
    var overTime: Int = 0,
    var isHolidayOccasion: Boolean = false,
    var isHolidayWeek: Boolean = false,
    var today: Boolean = false
)
