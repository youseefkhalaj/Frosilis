package com.youeefkhalaj.frosilis.ui.calendar

import com.youeefkhalaj.frosilis.data.calendarData.pojo.CalendarModel
import com.youeefkhalaj.frosilis.ui.item.PersonalDetails


data class CalendarUIState(
   val currentPersianDay: String = "",
   val listMonthCurrent: List<CalendarModel> = listOf(),

)
data class PersonalCalendarDetailsUiState(
   val outOfStock: Boolean = true,
   val personalDetails: PersonalDetails = PersonalDetails()
)
