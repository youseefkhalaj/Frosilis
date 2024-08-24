package com.youeefkhalaj.frosilis.data

import android.content.Context

interface AppContainer {
    val personalsRepository: PersonalsRepository
}
    /**
    * [AppContainer] implementation that provides instance of [OfflinePersonalsRepository]
    */
    class AppDataContainer(private val context: Context) : AppContainer {
        /**
         * Implementation for [PersonalsRepository]
         */
        override val personalsRepository: PersonalsRepository by lazy {
            OfflinePersonalsRepository(FrosilisDatabase.getDatabase(context).itemDao())
         }
    }