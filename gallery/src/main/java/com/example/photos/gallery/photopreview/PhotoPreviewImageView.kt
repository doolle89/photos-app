package com.example.photos.gallery.photopreview

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.photos.gallery.photopreview.PhotoPreviewImageView.InteractionState.*

class PhotoPreviewImageView : AppCompatImageView, View.OnTouchListener {

    interface OnZoomActiveListener {
        fun onZoomActiveStateChange(isActive: Boolean)
    }

    enum class InteractionState {
        None,
        Pan,
        Zoom
    }
    private var inputState = None

    private val scaleGestureDetector = ScaleGestureDetector(context, OnScaleGestureListener())

    private var scale = 1f
    private var minScale = 1f
    private var maxScale = 5f
    private val scaleMatrix = Matrix()
    private val scaleMatrixValues = FloatArray(9)

    private var contentWidth = 0f
    private var contentHeight = 0f
    private var viewWidth = 0
    private var viewHeight = 0

    private val previousTouchPoint = PointF()

    private var onZoomListener: OnZoomActiveListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        imageMatrix = scaleMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener(this)
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (scale == 1f) {
            fitToScreen()
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        val currentPoint = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousTouchPoint.set(currentPoint)
                inputState = Pan
            }
            MotionEvent.ACTION_MOVE -> if (inputState == Pan) {
                val dx = currentPoint.x - previousTouchPoint.x
                val dy = currentPoint.y - previousTouchPoint.y
                val translationXCorrected = correctPanTranslation(dx, viewWidth.toFloat(), contentWidth * scale)
                val translationYCorrected = correctPanTranslation(dy, viewHeight.toFloat(), contentHeight * scale)
                scaleMatrix.postTranslate(translationXCorrected, translationYCorrected)
                correctTranslation()
                previousTouchPoint[currentPoint.x] = currentPoint.y
            }
            MotionEvent.ACTION_POINTER_UP -> inputState = None
            MotionEvent.ACTION_UP -> inputState = None
        }
        imageMatrix = scaleMatrix
        return false
    }

    private  fun fitToScreen() {
        scale = 1f
        val drawable = drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        val scaleCorrected = scaleX.coerceAtMost(scaleY)
        val redundantXSpace = (viewWidth.toFloat() - scaleCorrected * imageWidth.toFloat()) / 2f
        val redundantYSpace = (viewHeight.toFloat() - scaleCorrected * imageHeight.toFloat()) / 2f
        scaleMatrix.setScale(scaleCorrected, scaleCorrected)
        scaleMatrix.postTranslate(redundantXSpace, redundantYSpace)
        contentWidth = viewWidth - 2 * redundantXSpace
        contentHeight = viewHeight - 2 * redundantYSpace
        imageMatrix = scaleMatrix
    }

    fun correctTranslation() {
        scaleMatrix.getValues(scaleMatrixValues)
        val translationX = scaleMatrixValues[Matrix.MTRANS_X]
        val translationY = scaleMatrixValues[Matrix.MTRANS_Y]
        val translationXCorrected = correctTranslation(translationX, viewWidth.toFloat(), contentWidth * scale)
        val translationYCorrected = correctTranslation(translationY, viewHeight.toFloat(), contentHeight * scale)
        if (translationXCorrected != 0f || translationYCorrected != 0f) {
            scaleMatrix.postTranslate(translationXCorrected, translationYCorrected)
        }
    }

    private fun correctTranslation(translation: Float, viewSize: Float, contentSize: Float): Float {
        var minTrans = 0f
        var maxTrans = 0f
        if (contentSize <= viewSize) {
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
        }
        if (translation < minTrans) {
            return -translation + minTrans
        }
        if (translation > maxTrans) {
            return -translation + maxTrans
        }
        return 0f
    }

    private fun correctPanTranslation(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) 0f else delta
    }

    fun setOnZoomListener(listener: OnZoomActiveListener) {
        onZoomListener = listener
    }

    private inner class OnScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            inputState = Zoom
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var scaleFactor = detector.scaleFactor
            val previousScale = scale
            scale *= scaleFactor
            if (scale > maxScale) {
                scale = maxScale
                scaleFactor = maxScale / previousScale
            } else if (scale < minScale) {
                scale = minScale
                scaleFactor = minScale / previousScale
            }
            if (contentWidth * scale <= viewWidth || contentHeight * scale <= viewHeight) {
                scaleMatrix.postScale(scaleFactor, scaleFactor, viewWidth / 2f, viewHeight / 2f)
            } else {
                scaleMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            }
            correctTranslation()
            if (onZoomListener != null) {
                if (scale == minScale && previousScale > minScale) {
                    onZoomListener?.onZoomActiveStateChange(false)
                } else if (scale > minScale && previousScale == minScale) {
                    onZoomListener?.onZoomActiveStateChange(true)
                }
            }
            return true
        }
    }
}