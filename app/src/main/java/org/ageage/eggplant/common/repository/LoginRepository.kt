package org.ageage.eggplant.common.repository

import org.ageage.eggplant.BuildConfig
import org.ageage.eggplant.common.oauth.HatenaOAuthManager

class LoginRepository {

    private val oAuthManager =
        HatenaOAuthManager(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET)

    fun fetchAuthorizationUrl() = oAuthManager.fetchAuthorizationUrl()

    fun fetchAccessToken(oAuthVerifier: String) = oAuthManager.fetchAccessToken(oAuthVerifier)

    fun fetchUserData() {

    }
}