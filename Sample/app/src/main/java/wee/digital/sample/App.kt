package wee.digital.sample

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import wee.digital.library.Library
import wee.digital.library.extension.SimpleActivityLifecycleCallbacks
import wee.digital.sample.data.firebase.appCheck
import wee.digital.widget.Widget
import java.lang.ref.WeakReference

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        app = this
        onFireAppInit()
        Library.init(this)
        Widget.init(this)
        app.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                activityReference = WeakReference(activity)
            }
        })
    }

}

lateinit var app: App private set

private var activityReference: WeakReference<Activity>? = null

val currentActivity: Activity? get() = activityReference?.get()

fun onFireAppInit(){
    FirebaseApp.initializeApp(app)
    appCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
}


