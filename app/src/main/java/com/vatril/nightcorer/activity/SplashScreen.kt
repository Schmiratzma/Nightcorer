package com.vatril.nightcorer.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * The Splashscreen that is shown on startup
 */
class SplashScreen :AppCompatActivity(){

    /**
     * Switches the the MainActivity as soon as the app loads
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}