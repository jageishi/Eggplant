package org.ageage.eggplant.common.oauth

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import io.reactivex.Single

private const val CALLBACK = "https://ageage.org"

class HatenaOAuthManager(
    private val consumerKey: String,
    private val consumerSecret: String
) {

    private var service: OAuth10aService? = null
    private var requestToken: OAuth1RequestToken? = null

    fun fetchAuthorizationUrl(): Single<String> {
        return Single.create {
            service = ServiceBuilder(consumerKey)
                .apiSecret(consumerSecret)
                .callback(CALLBACK)
                .build(HatenaOAuthApi())

            requestToken = service?.requestToken
            service?.getAuthorizationUrl(requestToken)?.let { token ->
                it.onSuccess(token)
            }
        }
    }

    fun fetchAccessToken(oauthVerifier: String): Single<String> {
        return Single.create {
            service?.let { oAuthService ->
                val accessToken =
                    oAuthService.getAccessToken(requestToken, oauthVerifier)
                it.onSuccess(accessToken.rawResponse)
            }
        }
    }

}