package com.valenpateltimesetu.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "TimeSetuPrefs",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_TUTORIAL_COMPLETED = "tutorial_completed"
    }
    
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
    
    fun isTutorialCompleted(): Boolean {
        return prefs.getBoolean(KEY_TUTORIAL_COMPLETED, false)
    }
    
    fun setTutorialCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_TUTORIAL_COMPLETED, completed).apply()
    }
    
    // Helper method to reset tutorial (for testing/debugging)
    fun resetTutorial() {
        prefs.edit().putBoolean(KEY_TUTORIAL_COMPLETED, false).apply()
    }
}

