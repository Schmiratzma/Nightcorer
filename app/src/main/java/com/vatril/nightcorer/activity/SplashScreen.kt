package com.vatril.nightcorer.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SplashScreen :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}