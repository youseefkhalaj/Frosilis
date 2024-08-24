package com.youeefkhalaj.frosilis.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youeefkhalaj.frosilis.data.Personal
import com.youeefkhalaj.frosilis.data.PersonalsRepository
import com.youeefkhalaj.frosilis.data.calendarData.di.DateModule
import com.youeefkhalaj.frosilis.data.calendarData.pojo.CalendarModel
import com.youeefkhalaj.frosilis.data.calendarData.repository.MonthGeneratorClass
import com.youeefkhalaj.frosilis.data.calendarData.utils.CalendarTool
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_DATE
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_OVERTIME
import com.youeefkhalaj.frosilis.ui.calendar.CalendarUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.GregorianCalendar

class HomeOverviewViewModel(personalsRepository: PersonalsRepository) :ViewModel() {


    val homeOverviewUiState: StateFlow<HomeOverviewUiState> =
        personalsRepository.getAllPersonalsStream().map { HomeOverviewUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeOverviewUiState()
            )




    private val _uiState = MutableStateFlow(CalendarUIState())
    val uiState: StateFlow<CalendarUIState> = _uiState.asStateFlow()

    private var calendar: GregorianCalendar = GregorianCalendar()
    private val calendarTools = CalendarTool(calendar)
    private val monthGeneratorClass = MonthGeneratorClass(calendarTools, DateModule.currentData)
    private val listIstaticMonth = createListCurrentMonth()

    private fun createListCurrentMonth(): List<CalendarModel> {
        val listDay = monthGeneratorClass.getCurrentMonthList()
        return listDay
    }


    fun listOverTime(overTime: Array<Array<IntArray>>):List<Int>{
        var toDay = false

             listIstaticMonth.map {
                 if (it.today == true) toDay = true
                 if (it.iranianDay == EMPTY_DATE){
                     it.overTime= EMPTY_DATE
                 }else{
            val b = overTime[it.iranianYear-1400][it.iranianMonth][it.iranianDay]
                     if (b == 0 && !toDay){
                         it.overTime = EMPTY_OVERTIME
                     }else{
                         it.overTime= b
                     }
                 }
         }
       val list =listIstaticMonth.map {
            it.overTime
        }
        return list
    }




    init {
        _uiState.value = CalendarUIState(
            currentPersianDay = calendarTools.toString(),
            listMonthCurrent = createListCurrentMonth())
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}
data class HomeOverviewUiState(val personalItemList: List<Personal> = listOf())