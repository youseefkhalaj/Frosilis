package com.youeefkhalaj.frosilis.data.calendarData.di


import com.youeefkhalaj.frosilis.data.calendarData.pojo.CalendarModel
import com.youeefkhalaj.frosilis.data.calendarData.utils.CalendarTool
import java.util.GregorianCalendar


/**
 * This object contains hilt modules needed for injection
 */

object DateModule {

    /**
     * Provides current date details
     */
    private val calendar = GregorianCalendar()
    private val calendarTool = CalendarTool(calendar)

    val currentData = with(calendarTool){
       CalendarModel(
           iranianDay  ,
           iranianMonth,
           iranianYear,
           dayOfWeek,
           gregorianDay,
           gregorianMonth,
           gregorianYear
       )
    }
}
