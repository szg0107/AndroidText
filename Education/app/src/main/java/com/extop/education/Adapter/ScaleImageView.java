package com.extop.education.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("FloatMath")
public class ScaleImageView extends android.support.v7.widget.AppCompatImageView {
    //自定义ImageView响应手势
    /*    <com.example.administrator.tbs.ScaleImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/image2"
        android:src="@mipmap/ic_launcher"
        android:scaleType="matrix" 必须写这个属性/>*/
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int FINISH = 3;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //原点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
            Log.d("Infor", "多点操作");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                matrix.set(getImageMatrix());
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d("Infor", "触摸了...");
                mode = NONE;
                this.setScaleType(ImageView.ScaleType.MATRIX);
                break;
            case MotionEvent.ACTION_UP:
                if (mode == NONE&&event.getX()>5&&event.getY()>5) {
                    setVisibility(View.INVISIBLE);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //多点触控
                oldDist = this.spacing(event);
                if (oldDist > 10f) {
                    Log.d("Infor", "oldDist" + oldDist);
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = FINISH;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == NONE || mode == DRAG) {     //此实现图片的拖动功能...
                    matrix.set(savedMatrix);
                    mode = DRAG;
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {// 此实现图片的缩放功能...
                    float newDist = spacing(event);
                    if (newDist > 10) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        setImageMatrix(matrix);
        return true;
    }
}