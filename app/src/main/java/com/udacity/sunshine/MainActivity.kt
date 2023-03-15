package com.udacity.sunshine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.sunshine.databinding.ActivityMainBinding

private const val TAG = "MainActivity-Truong"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}