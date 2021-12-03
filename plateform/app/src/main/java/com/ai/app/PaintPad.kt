package com.ai.app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintPad : View {
    private lateinit var mPaint: Paint
    private lateinit var mPath: Path
    private lateinit var mCanvas: Canvas
    private lateinit var mBitmap: Bitmap
    private var mIsLoaded = false
    private var mCurrentX = 0F
    private var mCurrentY = 0F

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        init(canvas!!.width, canvas!!.height)
        if (mIsLoaded) {
            canvas?.drawBitmap(mBitmap, 0F, 0F, mPaint)
            canvas?.drawPath(mPath, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var x = event?.x!!
        var y = event?.y!!

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mCurrentX = x
                mCurrentY = y
                mPath.moveTo(mCurrentX, mCurrentY)
            }

            MotionEvent.ACTION_MOVE -> {
                mCurrentX = x
                mCurrentY = y
                mPath.quadTo(mCurrentX, mCurrentY, x, y)
            }

            MotionEvent.ACTION_UP -> {
                mCanvas.drawPath(mPath, mPaint)
            }
        }

        invalidate()

        return true
    }

    private fun init(width: Int, height: Int) {
        if (!mIsLoaded) {
            mIsLoaded = true

            mPaint = Paint()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = 5.0F
            mPaint.style = Paint.Style.STROKE
            mPaint.color = Color.BLACK

            mPath = Path()

            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mCanvas = Canvas(mBitmap)
        }
    }
}