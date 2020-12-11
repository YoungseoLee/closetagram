package com.example.closetagram

import android.app.Application
import com.example.closetagram.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Closetagram : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Closetagram)
            modules(viewModelModule)
        }
    }

}