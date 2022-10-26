package org.ageage.eggplant

import android.app.Application
import com.facebook.stetho.Stetho
import org.ageage.eggplant.common.api.Client
import org.ageage.eggplant.common.db.AppDatabase
import timber.log.Timber

class DebugApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
        Client.setUp(this)
        AppDatabase.setUp(this)
    }
}