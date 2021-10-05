package ke.co.willynganga.googlesignin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ke.co.willynganga.googlesignin.repository.MainRepository

class MainViewModelFactory(
    val app: Application,
    val mainRepository: MainRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(app, mainRepository) as T
    }

}