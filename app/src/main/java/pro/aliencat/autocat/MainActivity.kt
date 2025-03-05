package pro.aliencat.autocat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pro.aliencat.autocat.di.appModule
import pro.aliencat.autocat.ui.app.App
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