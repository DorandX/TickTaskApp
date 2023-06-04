package com.example.ticktask.memoria

import android.app.Application
import android.content.Context


class AppContextProvider: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: AppContextProvider

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}
