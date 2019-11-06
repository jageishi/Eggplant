package org.ageage.eggplant.common.oauth

import com.github.scribejava.core.builder.api.DefaultApi10a

class HatenaOAuthApi : DefaultApi10a() {
    override fun getRequestTokenEndpoint(): String {
        return "https://www.hatena.com/oauth/initiate?scope=read_public,write_public"
    }

    override fun getAuthorizationBaseUrl(): String {
        return "https://www.hatena.ne.jp/touch/oauth/authorize"
    }

    override fun getAccessTokenEndpoint(): String {
        return "https://www.hatena.com/oauth/token"
    }
}