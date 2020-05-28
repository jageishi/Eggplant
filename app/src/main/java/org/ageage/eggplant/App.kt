package org.ageage.eggplant

import android.app.Application
import org.ageage.eggplant.data.api.Client

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Client.setUp()
    }
}