package com.youeefkhalaj.frosilis.ui.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youeefkhalaj.frosilis.data.PersonalsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class PersonalDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val personalsRepository: PersonalsRepository
) : ViewModel()
{
    private val personalId: Int = checkNotNull(savedStateHandle[PersonalItemDetailsDestination.personalItemIdArg])

    val uiState: StateFlow<PersonalDetailsUiState> =
        personalsRepository.getPersonalStream(personalId)
            .filterNotNull()
            .map {
                PersonalDetailsUiState(personalDetails = it.toPersonalDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PersonalDetailsUiState()
            )



    suspend fun deleteItem() {
        personalsRepository.deletePersonal(uiState.value.personalDetails.toPersonal())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class PersonalDetailsUiState(
    val personalDetails: PersonalDetails = PersonalDetails()
)

