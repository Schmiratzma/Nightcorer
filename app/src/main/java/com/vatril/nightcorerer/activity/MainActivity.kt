package com.vatril.nightcorerer.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.vatril.nightcorerer.R
import com.vatril.nightcorerer.view.adapter.CategoryAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)
        val pager = findViewById<ViewPager>(R.id.category_pager)
        pager.adapter = CategoryAdapter(supportFragmentManager)
    }
}
