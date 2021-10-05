package ke.co.willynganga.googlesignin.repository

import ke.co.willynganga.googlesignin.api.RetrofitInstance

class MainRepository {

    suspend fun sendIdToken(idToken: String) =
        RetrofitInstance.api.sendIdToken(idToken)

}