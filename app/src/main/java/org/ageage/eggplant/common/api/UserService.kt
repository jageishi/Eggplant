package org.ageage.eggplant.common.api

import io.reactivex.Single
import org.ageage.eggplant.common.api.response.User
import retrofit2.http.GET

interface UserService {
    @GET("/rest/1/my")
    fun userData(): Single<User>
}