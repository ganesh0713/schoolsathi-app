package com.schoolsathi.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.json.JSONObject

/**
 * Push notifications without a google-services.json baked into the APK: the app downloads the
 * Firebase settings from the school server and saves them (lib/core/push.dart). Firebase is started
 * here from those saved settings, so a notification can be shown even when the app is closed.
 */
class SchoolSathiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isNotEmpty()) return
            val saved = getSharedPreferences("FlutterSharedPreferences", MODE_PRIVATE)
                .getString("flutter.firebase_options", null) ?: return
            val o = JSONObject(saved)
            FirebaseApp.initializeApp(
                this,
                FirebaseOptions.Builder()
                    .setApiKey(o.getString("api_key"))
                    .setApplicationId(o.getString("app_id"))
                    .setGcmSenderId(o.getString("sender_id"))
                    .setProjectId(o.getString("project_id"))
                    .build(),
            )
        } catch (e: Exception) {
            Log.w("SchoolSathi", "Push notifications not started: ${e.message}")
        }
    }
}
