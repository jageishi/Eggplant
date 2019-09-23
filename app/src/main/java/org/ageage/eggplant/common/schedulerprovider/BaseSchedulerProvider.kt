package org.ageage.eggplant.common.schedulerprovider

import io.reactivex.Scheduler

interface BaseSchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}