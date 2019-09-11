package ru.vladislav_akulinin.mychat_version_2.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vladislav_akulinin.mychat_version_2.model.ProfileModel
import ru.vladislav_akulinin.mychat_version_2.repositories.PreferencesRepository

class ProfileViewModel : ViewModel() {

    private val repository: PreferencesRepository = PreferencesRepository
    private val profileDate = MutableLiveData<ProfileModel>()
    private val appTheme = MutableLiveData<Int>()
    private val repositoryError = MutableLiveData<Boolean>()
    private val isRepoError = MutableLiveData<Boolean>()

    init {
        Log.d("M_ProfileViewModel", "init view model")
        profileDate.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel", "view model cleared")
    }

    fun getProfileData() : LiveData<ProfileModel> = profileDate

    fun getTheme() : LiveData<Int> = appTheme

    fun getRepositoryError(): LiveData<Boolean> = repositoryError

    fun getIsRepoError():LiveData<Boolean> = isRepoError

    fun saveProfileData(profileModel:ProfileModel){
        repository.saveProfile(profileModel)
        profileDate.value = profileModel
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES){
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }else{
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun onRepoChanged(repository: String) {
        repositoryError.value = isRepoValid(repository)
    }


    fun onRepoEditCompleted(isError: Boolean) {
        isRepoError.value = isError
    }

    private fun isRepoValid(repoText: String): Boolean {
        val regex = """^(?:https://)?(?:www.)?(?:github.com/)[^/|\\s]+(?<!${getExceptionsWords()})(?:/)?$""".toRegex()
        return (repoText.isNotEmpty() && !regex.matches(repoText))
    }

    private fun getExceptionsWords(): String {
        return arrayOf(
            "enterprise", "features", "topics", "collections", "trending", "events", "marketplace", "pricing",
            "nonprofit", "customer-stories", "security", "login", "join"
        ).joinToString("|\\b","\\b")
    }
}