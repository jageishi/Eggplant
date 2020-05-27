package org.ageage.eggplant

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        org.ageage.eggplant.repository.api.Client.setUp()
    }
}