package com.youeefkhalaj.frosilis.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.youeefkhalaj.frosilis.FrosilisApplication
import com.youeefkhalaj.frosilis.ui.calendar.PersonalOverTimeViewModel
import com.youeefkhalaj.frosilis.ui.home.HomeOverviewViewModel
import com.youeefkhalaj.frosilis.ui.home.HomeViewModel
import com.youeefkhalaj.frosilis.ui.item.PersonalDetailsViewModel
import com.youeefkhalaj.frosilis.ui.item.PersonalEditViewModel
import com.youeefkhalaj.frosilis.ui.item.PersonalEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            PersonalEntryViewModel(frosilisAppliction().container.personalsRepository)
        }

        initializer {
            PersonalEditViewModel(
                this.createSavedStateHandle(),
                frosilisAppliction().container.personalsRepository
            )
        }
        initializer {
            PersonalOverTimeViewModel(
                this.createSavedStateHandle(),
                frosilisAppliction().container.personalsRepository
            )
        }


        initializer {
            PersonalDetailsViewModel(
                this.createSavedStateHandle(),
                frosilisAppliction().container.personalsRepository
            )
        }
        initializer {
            HomeOverviewViewModel(frosilisAppliction().container.personalsRepository)
        }


    // Initializer for HomeViewModel
    initializer {
        HomeViewModel(frosilisAppliction().container.personalsRepository)
      }
    }
}

fun CreationExtras.frosilisAppliction() =
(this[AndroidViewModelFactory.APPLICATION_KEY] as FrosilisApplication)