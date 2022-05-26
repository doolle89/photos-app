package com.example.photos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photos.gallery.GalleryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, GalleryFragment())
                .commitNow()
        }
    }
}