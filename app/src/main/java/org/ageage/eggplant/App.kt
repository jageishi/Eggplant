package org.ageage.eggplant

import android.app.Application
import org.ageage.eggplant.common.api.Client
import org.ageage.eggplant.common.db.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Client.setUp(this)
        AppDatabase.setUp(this)
    }
}