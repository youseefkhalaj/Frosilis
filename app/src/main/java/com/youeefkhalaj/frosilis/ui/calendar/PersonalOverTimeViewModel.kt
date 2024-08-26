package com.youeefkhalaj.frosilis.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youeefkhalaj.frosilis.data.PersonalsRepository
import com.youeefkhalaj.frosilis.data.Shift
import com.youeefkhalaj.frosilis.data.calendarData.di.DateModule
import com.youeefkhalaj.frosilis.data.calendarData.pojo.CalendarModel
import com.youeefkhalaj.frosilis.data.calendarData.pojo.MonthType
import com.youeefkhalaj.frosilis.data.calendarData.repository.MonthGeneratorClass
import com.youeefkhalaj.frosilis.data.calendarData.utils.CalendarTool
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_DATE
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_OVERTIME
import com.youeefkhalaj.frosilis.data.calendarData.utils.REQUEST_REJECTION
import com.youeefkhalaj.frosilis.ui.item.toPersonal
import com.youeefkhalaj.frosilis.ui.item.toPersonalDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.GregorianCalendar

class PersonalOverTimeViewModel(
    savedStateHandle: SavedStateHandle,
    private val personalsRepository: PersonalsRepository
): ViewModel(

) {
    private val _uiState = MutableStateFlow(CalendarUIState())
    val uiState: StateFlow<CalendarUIState> = _uiState.asStateFlow()

    private val personalId: Int = checkNotNull(savedStateHandle[CalendarEditDestination.itemIdArg])

    var calendarPersonalUiState: StateFlow<PersonalCalendarDetailsUiState> =
        personalsRepository.getPersonalStream(personalId)
            .filterNotNull()
            .map {
                PersonalCalendarDetailsUiState(personalDetails = it.toPersonalDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PersonalCalendarDetailsUiState()
            )





    private var calendar: GregorianCalendar = GregorianCalendar()
    private val calendarTools = CalendarTool(calendar)
    private val monthGeneratorClass = MonthGeneratorClass(calendarTools, DateModule.currentData)
    private val listIstaticMonth = createListCurrentMonth()



    fun nextMonth() {
        val listDay = monthGeneratorClass.getMonthList(MonthType.NEXT_MONTH)
        _uiState.update {
            it.copy(
                listMonthCurrent = listDay
            )
        }
    }

    fun previousMonth() {
        val listDay = monthGeneratorClass.getMonthList(MonthType.PREVIOUS_MONTH)
        _uiState.update {
            it.copy(
                listMonthCurrent = listDay
            )
        }


    }

    private fun createListCurrentMonth(): List<CalendarModel> {
        val listDay = monthGeneratorClass.getCurrentMonthList()
        return listDay
    }

    fun addOverTimeToListDay(overTime:Array<Array<IntArray>>){
        val list = createListCurrentMonth()
        var toDay = false
        var toDay1 = false
        var overtime: Int

        list.map {
            if (it.today){ toDay = true}
        }
            if (toDay){
                list.map {
                    if (it.iranianDay != EMPTY_DATE) {
                        if(it.today){toDay1 = true}
                        overtime = overTime[it.iranianYear - 1400][it.iranianMonth][it.iranianDay]
                        if (overtime == 0 && !toDay1) {
                            it.overTime = EMPTY_OVERTIME
                        } else {
                            it.overTime = overtime
                        }
                    }
                    _uiState.update {
                        it.copy(
                            listMonthCurrent = list
                        )
                    }
                }
         }else{
            list.map {

                if (it.iranianDay != EMPTY_DATE) {
                    it.overTime = overTime[it.iranianYear-1400][it.iranianMonth][it.iranianDay]

                }
                _uiState.update {
                    it.copy(
                        listMonthCurrent = list
                    )
                }
            }

        }
    }

    fun addExtraOverTime(extra: Int){
        viewModelScope.launch {

            val currentOver = calendarPersonalUiState.value.personalDetails.toPersonal()
            personalsRepository.updatePersonal(currentOver.copy(overTimeAddCurrentMonth = extra))

        }

    }


    fun overTimeForCurrentMonth(overTime:Array<Array<IntArray>>) {
        val list = listIstaticMonth
        var overtimeForMonth = 0
        list.map {
            if (it.iranianDay != EMPTY_DATE) {

                    it.overTime = overTime[it.iranianYear - 1400][it.iranianMonth][it.iranianDay]

            }
        if (it.overTime != REQUEST_REJECTION) {
            overtimeForMonth += it.overTime
        }
        }
        viewModelScope.launch {
            val currentState = calendarPersonalUiState.value.personalDetails.toPersonal()
            personalsRepository.updatePersonal(currentState.copy(currentMonthOverTime = overtimeForMonth))
        }
    }


init {
    _uiState.value = CalendarUIState(currentPersianDay = calendarTools.toString(), listMonthCurrent = createListCurrentMonth())

}

    fun updateOverTime(y: Int, m: Int, d: Int, input: Int){
        viewModelScope.launch {
            val currentHorseOverTime = calendarPersonalUiState.value.personalDetails.horseOvertime
            currentHorseOverTime[y][m][d]=input
            val currentOver = calendarPersonalUiState.value.personalDetails.toPersonal()
            personalsRepository.updatePersonal(currentOver.copy(horseOvertime = currentHorseOverTime ))
        }

    }

    fun addShiftPersonal(shift: Shift) {
        val list = uiState.value.listMonthCurrent
        val calShift = CalculateShift(shift)
        with(calShift) {
            list.map {
                it.shift = calculateDaysYear(it.iranianYear, it.iranianMonth, it.iranianDay)
            }
        }
        _uiState.update {
            it.copy(listMonthCurrent = list)
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
      }
}











