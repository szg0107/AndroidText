package com.example.administrator.firstlineofcodetest.MyImage;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;


/**
 * Created by Administrator on 2017/5/30.
 * 自定义ImageView
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    //初始化变量
    private boolean mOnce;

    //初始化时缩放的值（缩小的边界）
    private float mInitScale;
    //双击放大时到达的值
    private float mMidScale;
    //放大的最大值
    private float mMaxScale;
    //进行平移缩放的类
    private Matrix mScaleMatrix;


    //多点触控的类（捕获用户多点触控时缩放的比例）
    private ScaleGestureDetector mScaleGestureDetector;


    //自由移动
    //记录上一次多点触控的数量
    private int mLastPointerCount;

    private float mLastX;
    private float mLastY;

    private int mTouchSlop;
    private boolean isCanDrag;

    private RectF matrixRectF;
    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    //双击放大与缩小
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;

    //一个参数调用两个参数的构造方法
    public MyImageView(Context context) {
        this(context, null);
    }

    //两个参数调用三个参数的构造方法
    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //所有操作、初始化操作代码都写在三个参数的构造方法中 通过任意构造函数都会执行初始化代码
    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init
        //初始化缩放平移类
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //避免用户疯狂点击
                if (isAutoScale) {
                    return true;
                }
                float x = e.getX();
                float y = e.getY();
                if (getScale() < mMidScale) {
//                    mScaleMatrix.postScale(mMidScale / getScale(), mMidScale / getScale(), x, y);
//                    setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                    isAutoScale = true;
                } else {
//                    mScaleMatrix.postScale(mInitScale / getScale(), mInitScale / getScale(), x, y);
//                    setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                    isAutoScale = true;
                }
                return true;
            }
        });
    }

    //自动放大与缩小
    private class AutoScaleRunnable implements Runnable {
        //缩放的目标值
        private float mTargetScale;
        //缩放的中心点
        private float x;
        private float y;
        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        private float tmpScale;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            }
            if (getScale() > mTargetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            //进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBoderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            float currentScale = getScale();
            if ((tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                postDelayed(this, 16);
            } else {
                //设置为我们的目标值
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBoderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //添加onGlobalLayout监听
        getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //移除onGlobalLayout监听
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }


    /*获取ImageView加载完成的图片*/
    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            //得到控件的宽和高，一般情况下控件的宽和高等于屏幕的宽和高
            int width = getWidth();
            int height = getHeight();


            //得到图片，并且得到图片的宽和高
            Drawable drawable = getDrawable();
            //如果没有设置图片 直接return
            if (drawable == null) {
                return;
            }
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            //设置默认缩放值为1.0f
            float scale = 1.0f;
            //如果图片的宽度大于控件的宽度，但是高度小于控件的高度，我们将其缩小
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }
            //如果图片的高度大于控件的高度，但是宽度小于控件的宽度，我们将其缩小
            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }
            //如果图片的宽高都大于控件的宽高，或者图片的宽高都小于控件的宽高，取缩放的最小值，将其缩放至控件内部
            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            //得到了初始化时缩放的比例
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;

            //将图片移动至控件的中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;
            //进行平移
            mScaleMatrix.postTranslate(dx, dy);
            //进行缩放
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }
    }


    /*获取当前图片的缩放值*/
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /*缩放进行中
    缩放的区间：initScale maxScale*/
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //拿到当前缩放的值
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        if (getDrawable() == null) {
            return true;
        }

        //缩放范围的控制
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            //缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBoderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    //获得图片放大缩小以后的宽和高，以及上下左右
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    //在缩放的时候进行边界控制以及位置的控制
    private void checkBoderAndCenterWhenScale() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        //缩放时进行边界检测，防止出现白边
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        //如果宽或者高小于控件的宽和高；则让其居住
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f;
        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    //缩放开始 必须return true
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    //缩放结束
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    //将触摸的event传给ScaleGestureDetector处理
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);

        //存储多点触控的中心点
        float x = 0;
        float y = 0;
        //拿到多点触控的数量
        int ponterCount = event.getPointerCount();
        for (int i = 0; i < ponterCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= ponterCount;
        y /= ponterCount;
        if (mLastPointerCount != ponterCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = ponterCount;
        //获得用户缩放以后的宽高
        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //宽度或高度大于屏幕的宽度或高度时viewPager不拦截移动事件
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    //请求不被拦截
                    if (getParent() instanceof ViewPager){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    //请求不被拦截
                    if (getParent() instanceof ViewPager){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (isCanDrag) {
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        //如果宽度小于控件宽度，不允许横向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        //如果高度小于控件高度，不允许纵向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }
        return true;
    }

    //当移动时进行边界检测
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    //判断是否足以触发move
    private boolean isMoveAction(float dx, float dy) {

        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
