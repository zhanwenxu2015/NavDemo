package com.example.mylibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavInflater
import androidx.navigation.fragment.NavHostFragment

class LibActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib)
        (supportFragmentManager.findFragmentById(R.id.nav_lib_fragment) as NavHostFragment).navController.apply {
            this.graph = NavInflater(this@LibActivity, this.navigatorProvider).inflate(R.navigation.nav_lib)
        }
    }
}