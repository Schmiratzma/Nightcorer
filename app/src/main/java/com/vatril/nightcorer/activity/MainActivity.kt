package com.vatril.nightcorer.activity

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.*
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.vatril.nightcorer.R
import com.vatril.nightcorer.view.adapter.AsyncCategoryAdapterLoader

private const val requestCode = 89543

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            showPermissionRequestDialog()
            findViewById<TextView>(R.id.loading_text).text = getString(R.string.permission_required)
        }else{
            initList()
        }
    }

    private fun initList() {
        findViewById<TextView>(R.id.loading_text).text = getString(R.string.loading)
        val pager = findViewById<ViewPager>(R.id.category_pager)
        AsyncCategoryAdapterLoader(supportFragmentManager,{
            if(it != null){
                pager.adapter = it
                pager.alpha = 0f
                pager.visibility = View.VISIBLE
                pager.animate().alpha(1f).duration = 1
                findViewById<ConstraintLayout>(R.id.loading_layout).animate().alpha(0f).duration=250
            }
        }).execute()


    }

    private fun showPermissionRequestDialog() =
            AlertDialog.Builder(this).setTitle(getString(R.string.permission_required)).setCancelable(false)
                    .setMessage(getString(R.string.permission_reason)).setNeutralButton(getString(R.string.ok), { _, _ ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
                        }
                    }).create().show()

    override fun onRequestPermissionsResult(requestRequestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestRequestCode == requestCode) {
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    initList()
                    return
                }
            }
            showPermissionRequestDialog()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
