package com.youeefkhalaj.frosilis.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youeefkhalaj.frosilis.data.PersonalsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PersonalEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val personalsRepository: PersonalsRepository
) : ViewModel() {

    var personalUiState by mutableStateOf(PersonalUiState())
          private set

    private val personalId: Int = checkNotNull(savedStateHandle[PersonalEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            personalUiState = personalsRepository.getPersonalStream(personalId)
                .filterNotNull()
                .first()
                .toPersonalUiState(true)
        }
    }
    suspend fun updateItem() {
        if (validateInput(personalUiState.personalDetails)) {
            personalsRepository.updatePersonal(personalUiState.personalDetails.toPersonal())
        }
    }


    fun updateUiState(personalDetails: PersonalDetails){
        personalUiState =
            PersonalUiState(personalDetails = personalDetails, isEntryValid = validateInput())
    }
    private fun validateInput(uiState: PersonalDetails = personalUiState.personalDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && image != null
        }
    }

}


