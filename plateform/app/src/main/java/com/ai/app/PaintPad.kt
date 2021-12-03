package com.ai.app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap

import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
            canvas?.drawBitmap(mBitmap, 0F, 0F, null)
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

    fun save() {
        var bitmap = resizeBitmap(mBitmap, 28F, 28F)
        //指定我们想要存储文件的地址
        val targetPath = context.externalCacheDir.toString()
        //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
        val saveFile = File(targetPath, "test.png")
        try {
            val saveImgOut = FileOutputStream(saveFile)
            // compress - 压缩的意思
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, saveImgOut)
            //存储完成后需要清除相关的进程
            saveImgOut.flush()
            saveImgOut.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Float, height: Float): Bitmap {
        var originWidth = bitmap.width
        var originHeight = bitmap.height

        var scaleWidth = width / originWidth
        var scaleHeight = height / originHeight

        var matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, originWidth, originHeight, matrix, true)
    }

    private fun init(width: Int, height: Int) {
        if (!mIsLoaded) {
            mIsLoaded = true

            mPaint = Paint()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = 15.0F
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeCap = Paint.Cap.ROUND
            mPaint.color = Color.BLACK

            mPath = Path()

            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mCanvas = Canvas(mBitmap)
        }
    }
}