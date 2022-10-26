package org.ageage.eggplant.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.ageage.eggplant.common.repository.LoginRepository
import org.ageage.eggplant.common.schedulerprovider.SchedulerProvider

class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass != LoginViewModel::class.java) {
            throw IllegalArgumentException("Illegal ViewModel class.")
        }

        return LoginViewModel(
            LoginRepository(),
            SchedulerProvider()
        ) as T
    }
}