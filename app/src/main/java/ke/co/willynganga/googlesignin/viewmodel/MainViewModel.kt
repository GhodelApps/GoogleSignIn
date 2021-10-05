package ke.co.willynganga.googlesignin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ke.co.willynganga.googlesignin.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(
    app: Application,
    private val mainRepository: MainRepository
): AndroidViewModel(app) {

    val sendIdTokenResponse: MutableLiveData<String> = MutableLiveData()

    fun sendIdToken(idToken: String) =
        viewModelScope.launch {
            val response = mainRepository.sendIdToken(idToken)

            if (response.isSuccessful) {
                response.body()?.let {
                    sendIdTokenResponse.postValue(it)
                }
            } else {
                sendIdTokenResponse.postValue("No response.")
            }
        }

}