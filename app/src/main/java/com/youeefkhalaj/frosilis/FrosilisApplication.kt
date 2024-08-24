package com.youeefkhalaj.frosilis

import android.app.Application
import com.youeefkhalaj.frosilis.data.AppContainer
import com.youeefkhalaj.frosilis.data.AppDataContainer

class FrosilisApplication: Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
