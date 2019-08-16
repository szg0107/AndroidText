package com.extop.HangTianTianQiTong.Adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.extop.HangTianTianQiTong.MyApplication;

/**
 * Created by Administrator on 2017/5/9.
 * 解决键盘挡住输入框
 */

public class SoftHideKeyBoardUtil {
    public static void assistActivity(Activity activity) {
        new SoftHideKeyBoardUtil(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private SoftHideKeyBoardUtil(Activity activity) {
        //1.找到Activity的最外层布局控件，它其实是一个Decorview,它所用的控件就是FrameLayout
        FrameLayout content= (FrameLayout) activity.findViewById(android.R.id.content);
        //2.获取到setContentView放进去的View
        mChildOfContent=content.getChildAt(0);
        //3.给Activity的XML布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            //4.软键盘弹起会使GlobalLayout发生变化
            public void onGlobalLayout() {
                //5.当前布局发生变化时，对Activity的Xml布局进行重绘
                possiblyResizeChildOfContent();
            }
        });
        //6.获取到Activity的XML布局的放置参数
        frameLayoutParams= (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }
    //获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
    private void possiblyResizeChildOfContent(){
        //1.获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int usableHeightNow=computeUsableHeight();
        //2.如果当前可用高度和原始值不一样
        if (usableHeightNow!=usableHeightPrevious){
            //3.获取Activity中XML中布局在当前界面显示的高度
            int usableHeightSansKeyboard=mChildOfContent.getRootView().getHeight();
            //4.Activity中XML布局的高度-当前可用的高度
            int heightDifference=usableHeightSansKeyboard-usableHeightNow;
            //5.高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference>(usableHeightSansKeyboard/4)){
                //6.键盘弹出了，Activity的XML布局高度应当减去键盘高度
                frameLayoutParams.height=usableHeightSansKeyboard-heightDifference;
            }else {
                //6.键盘收起了，Activity的XML布局高度应当和可用高度一样
                frameLayoutParams.height=usableHeightSansKeyboard;
            }


            if (MyApplication.is_Virtual_key) {
                //如果两次高度不一致  usableHeightNow != usableHeightPrevious


//            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                // keyboard probably just became visible
//                isKeyBordVisiable=true;
//            } else {
//                // keyboard probably just became hidden
//                isKeyBordVisiable=false;
//            }
                //将计算的可视高度设置成视图的高度
                frameLayoutParams.height = usableHeightNow;
//                mChildOfContent.requestLayout();//请求重新布局
//                usableHeightPrevious = usableHeightNow;
            }

            //7.重绘Activity的XML布局
            mChildOfContent.requestLayout();
            usableHeightPrevious=usableHeightNow;
        }
    }



    private int computeUsableHeight(){
        Rect r=new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        //全屏模式下：直接返回r.bottom,r.top其实是状态栏的高度
        return (r.bottom);
    }
}
