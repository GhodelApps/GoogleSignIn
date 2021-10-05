package ke.co.willynganga.googlesignin.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserManagementAPI {

    @POST("/auth/google/user")
    suspend fun sendIdToken(@Body idToken: String): Response<String>

}