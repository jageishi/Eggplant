package org.ageage.eggplant.common.schedulerprovider

import io.reactivex.schedulers.Schedulers

class TrampolineSchedulerProvider : BaseSchedulerProvider {
    override fun io() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
}