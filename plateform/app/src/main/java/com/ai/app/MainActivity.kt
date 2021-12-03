package com.ai.app

import android.app.Activity
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {
    private var mPaintPad: PaintPad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPaintPad = findViewById(R.id.paint_pad)
    }

    fun onRecognizeClick(view: View) {
        mPaintPad?.save()
    }
}