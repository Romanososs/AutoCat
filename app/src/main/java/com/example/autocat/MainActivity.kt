package com.example.autocat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.autocat.di.appModule
import com.example.autocat.ui.app.App
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinApplication(application = {
                androidContext(this@MainActivity.applicationContext)
                modules(appModule)
            }) {
                App()
            }
        }
    }
}