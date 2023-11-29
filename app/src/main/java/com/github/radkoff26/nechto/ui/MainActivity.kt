package com.github.radkoff26.nechto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.radkoff26.nechto.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}