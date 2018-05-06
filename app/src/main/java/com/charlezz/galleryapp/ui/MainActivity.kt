package com.charlezz.galleryapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.charlezz.galleryapp.R
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.toObservable

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName

    val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissions.toObservable()
                .filter {
                    ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
                }
                .toList()
                .map {
                    permissions.sortedArray()
                }
                .subscribe { permissionList: Array<String>?, _: Throwable? ->
                    permissionList?.let {
                        ActivityCompat.requestPermissions(this, it, 0)
                    }
                }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Observables.zip(
                permissions.toObservable(),
                grantResults.toObservable())
                .filter {
                    it.second != PackageManager.PERMISSION_GRANTED
                }.count()
                .subscribe { permissionCount: Long?, t2: Throwable? ->
                    if (permissionCount == 0L) {//zero means all permissions granted
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.container, GalleryFragment.newInstance())
                                .commit()
                    } else {
                        finish()
                    }
                }


    }


}
