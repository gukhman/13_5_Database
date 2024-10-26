package com.example.a13_5_database

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : BaseActivity() {

    private lateinit var startBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindowInsets(R.id.main)
        setupToolbar(R.id.toolbar, false)

        init()

        startBTN.setOnClickListener {
            val intent = Intent(this, DatabaseActivity::class.java)
            startActivity(intent)
        }

    }

    private fun init() {
        startBTN = findViewById(R.id.startBTN)
    }
}