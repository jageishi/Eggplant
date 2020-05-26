package org.ageage.eggplant

import android.app.Application
import org.ageage.eggplant.data.api.Client
import timber.log.Timber

class DebugApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Client.setUp()
    }
}