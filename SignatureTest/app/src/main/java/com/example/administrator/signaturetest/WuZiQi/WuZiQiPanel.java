package com.example.administrator.signaturetest.WuZiQi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 * 自定义五子棋view
 */

public class WuZiQiPanel extends View {
    private int mPanelWidth;//棋盘宽度
    private float mLineHeight;//行高
    private int MAX_LINE = 10;//最大多少行
    private int MAX_COUNT_IN_LINE = 5;
    private Paint mPaint = new Paint();
    private Bitmap mWhitePice;//白棋子
    private Bitmap mBlackPice;//黑棋子
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;//棋子的大小是行高的3/4
    private boolean mIsWhite = true;//白棋先手，当前轮到白棋
    private ArrayList<Point> mWhiteArray = new ArrayList<>();//存储白棋的横纵坐标
    private ArrayList<Point> mBlackArray = new ArrayList<>();//存储黑棋的横纵坐标
    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    public WuZiQiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置背景颜色
        //setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        //设置mPant颜色 半透明灰色
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        //绘制棋子
        mWhitePice = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPice = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获得宽度的size、mode
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获得高度的size、mode
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //传入精确值时
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    //当宽高确定发生改变时会回调
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //背景的尺寸
        mPanelWidth = w;
        //*1.0f转化成浮点数
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;
        //棋子的尺寸
        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);//目标的宽度
        mWhitePice = Bitmap.createScaledBitmap(mWhitePice, pieceWidth, pieceWidth, false);
        mBlackPice = Bitmap.createScaledBitmap(mBlackPice, pieceWidth, pieceWidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction();
        //当用户按下子view时交给子view处理
        if (action == MotionEvent.ACTION_UP) {
            //获取用户点击的坐标
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            //判断用户点击的位置有没有棋子
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            //重绘
            invalidate();
            //改变IsWhite的值
            mIsWhite = !mIsWhite;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    //绘制界面
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    //判断游戏是否结束
    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        if (whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;
            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            alert = null;
            builder = new AlertDialog.Builder(getContext());
            alert = builder.setTitle("恭喜您：")
                    .setMessage(text)
                    .setNegativeButton("再来一局", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            start();
                        }
                    }).create();             //创建AlertDialog对象
            alert.show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, points);
            if (win) {
                return true;
            }
            win = checkVertical(x, y, points);
            if (win) {
                return true;
            }
            win = checkLeftDiagonal(x, y, points);
            if (win) {
                return true;
            }
            win = checkRightDiagonal(x, y, points);
            if (win) {
                return true;
            }
        }
        return false;
    }
    public void start(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver=false;
        mIsWhiteWinner=false;
        invalidate();
    }
    //判断x,y位置的棋子，是否横向有相邻的五个一致
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        //左
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        //右
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        //上
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        //下
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //上
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        //下
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //上
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        //下
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return false;
    }

    //绘制棋子
    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePice,
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPice,
                    (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
    }

    //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for (int i = 0; i < MAX_LINE; i++) {
            //横坐标起始位置
            int startX = (int) (lineHeight / 2);
            //横坐标结束位置
            int endX = (int) (w - lineHeight / 2);
            //纵坐标位置
            int y = (int) ((0.5 + i) * lineHeight);
            //绘制横线
            canvas.drawLine(startX, y, endX, y, mPaint);
            //绘制纵线
            // 纵线起点x坐标等于横线的y，y坐标等于横线的startX,终点的X坐标等于横线的y，y坐标等横线的endX
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }
    //数据的存储与恢复
    private static final String INSTANCE="instance";
    public static final String INSTANCE_GAME_OVER="instance_game_over";
    public static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    public static final String INSTANCE_BLACK_ARRAY="instance_black_array";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Build){
            Bundle bundle= (Bundle) state;
            mIsGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
