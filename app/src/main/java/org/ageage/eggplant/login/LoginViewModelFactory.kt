package org.ageage.eggplant.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != LoginViewModel::class.java) {
            throw IllegalArgumentException("Illegal ViewModel class.")
        }

        return LoginViewModel() as T
    }
}