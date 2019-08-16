package ru.vladislav_akulinin.mychat_version_2

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

//единная точка входа. Класс, который вызывается при старте приложения
//в нем создаем объект контекста, при старте приложения
class App : Application(){
    companion object{
        private var instance:App? = null

        fun applicationContext() : Context{
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
//        //установка текущей темы без пересборки
//        PreferencesRepository.getAppTheme().also {
//            AppCompatDelegate.setDefaultNightMode(it)
//        }
    }
}