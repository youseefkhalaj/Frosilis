package com.youeefkhalaj.frosilis.ui.item

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.youeefkhalaj.frosilis.data.Personal
import com.youeefkhalaj.frosilis.data.PersonalsRepository
import com.youeefkhalaj.frosilis.data.Shift


class PersonalEntryViewModel(private val personalsRepository: PersonalsRepository): ViewModel(){

    var personalUiState by mutableStateOf(PersonalUiState())
        private set

    fun updateUiState(personalDetails: PersonalDetails) {
        personalUiState = PersonalUiState(personalDetails = personalDetails, isEntryValid = validateInput(personalDetails))
    }
    suspend fun savePersonal() {
        if (validateInput()) {
            personalsRepository.insertPersonal(personalUiState.personalDetails.toPersonal())
        }
    }


    private fun validateInput(uiState: PersonalDetails = personalUiState.personalDetails): Boolean {
       return with(uiState) {
            name.isNotBlank() && image != null
        }
    }
}








data class PersonalUiState(
    val personalDetails: PersonalDetails = PersonalDetails(),
    val isEntryValid: Boolean = false
)




data class PersonalDetails (
    val id: Int = 0,
    val name: String = "",
    val image: Bitmap? = null,
    val currentMonthOvertime: Int = 0,
    val overTimeAddCurrentMonth: Int = 0,
    val horseOvertime: Array<Array<IntArray>> = Array(20){Array(14){IntArray(40){0 } } },
    val shift: Shift = Shift.F
)


fun PersonalDetails.toPersonal(): Personal = Personal(
    id = id,
    name = name,
    image = image,
    currentMonthOverTime = currentMonthOvertime,
    overTimeAddCurrentMonth = overTimeAddCurrentMonth,
    horseOvertime = horseOvertime,
    shift = shift

)


fun Personal.toPersonalUiState(isEntryValid: Boolean = false): PersonalUiState = PersonalUiState(
    personalDetails = this.toPersonalDetails(),
    isEntryValid = isEntryValid
)



fun Personal.toPersonalDetails(): PersonalDetails = PersonalDetails(
    id = id,
    name = name,
    image = image,
    currentMonthOvertime = currentMonthOverTime,
    overTimeAddCurrentMonth = overTimeAddCurrentMonth,
    horseOvertime = horseOvertime,
    shift = shift

)