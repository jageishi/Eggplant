package org.ageage.eggplant.common.repository

import org.ageage.eggplant.BuildConfig
import org.ageage.eggplant.common.api.Client
import org.ageage.eggplant.common.api.UserService
import org.ageage.eggplant.common.db.AppDatabase
import org.ageage.eggplant.common.db.entity.User
import org.ageage.eggplant.common.enums.Endpoint
import org.ageage.eggplant.common.oauth.HatenaOAuthManager

class LoginRepository {

    private val oAuthManager =
        HatenaOAuthManager(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET)

    fun fetchAuthorizationUrl() = oAuthManager.fetchAuthorizationUrl()

    fun fetchAccessToken(oAuthVerifier: String) = oAuthManager.fetchAccessToken(oAuthVerifier)

    fun fetchUser(token: String, tokenSecret: String) =
        Client.oAuthClient(Endpoint.HATENA_API, token, tokenSecret)
            .create(UserService::class.java)
            .userData()

    fun saveUser(user: User) {
        AppDatabase.getInstance()
            .userDao()
            .insert(user)
    }
}