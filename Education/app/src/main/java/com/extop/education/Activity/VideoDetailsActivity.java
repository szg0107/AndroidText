package com.extop.education.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.extop.education.Adapter.MyImageView;
import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.ScaleImageView;
import com.extop.education.Adapter.SharedHelper;
import com.extop.education.MyApplication;
import com.extop.education.R;
import com.extop.education.wxapi.Constants;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.common.Callback;
import org.xutils.x;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;


/**
 * Created by Administrator on 2016/12/6.
 * 公司必修
 */

public class VideoDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_video, tv_frequency, tv_pdf, tv_Image, tv_videoTitle;
    Toolbar tb_detailsIcon;
    XWalkView wv_VideoDetails;
    Intent intent;
    private ScaleImageView scaleImageView;
    //1.图片地址 2.资源类型 3.重复使用（zero或one） 4.是否开启类目 5.类目ID
    private String imgUrl = "", type = "avi", reuse = "", isOpenCategory = "", catalogueId = "";
    //1.资源类型数组 2.资源名称数组 3.资源顺序数组 4.资源内容数量数组 5.是否开启类目数组
    String[] typeArray = {"avi", "png", "mp3", "pdf"}, name, order, number, isOpenCategoryArray;
    //资源菜单数组
    Object[] controlsArray = {tv_video, tv_frequency, tv_pdf, tv_Image};
    //搜索控件
    private SearchView sw_searchBox;
    //读取本地文件内容
    private SharedHelper sharedHelper;
    //无图模式是否开启
    private boolean isOpen = true;



    //在消息队列中实现对控件的更改
    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    viewPager.setVisibility(View.VISIBLE);
                    viewPager.setAdapter(pagerAdapter);
                    break;
            }
        }
    };

    //上拉加载更多控件
    SwipeToLoadLayout swipeToLoadLayout;
    //上拉加载更多需要的线程
//    Handler handler;

    //微信API
    private IWXAPI api;
    private SendMessageToWX.Req req;
    private Bitmap bitmapShare;
    //悬浮框
    private PopupWindow mPopWindow;


    private ViewPager viewPager;
    //给ViewPager设置数据源和适配器
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private List<MyImageView> myImageViews = new ArrayList<MyImageView>();
    private MyImageView myImageView = null;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置状态栏的背景色
        Window window = getWindow();
        NetWorkTools.taskbar_transparent(window, this);


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_details);
        //键盘弹起窗口可拉伸
        assistActivity(this);

        //初始化控件
        init();

        //给SearchView设置输入监听事件
        sw_searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
//                wv_MyGroup.loadUrl("javascript:search('"+query.trim()+"')");
                Intent intent = new Intent("android.intent.Activity.CircleInsideSearch");
                intent.putExtra("content", query.trim());
                intent.putExtra("name", name);
                intent.putExtra("number", number);
                intent.putExtra("typeArray", typeArray);
                intent.putExtra("isOpen", isOpen);
                intent.putExtra("reuse", reuse);
                intent.putExtra("catalogueId", catalogueId);
                wv_VideoDetails.loadUrl("javascript:SearchRecords(" + MyApplication.circleID + ",'" + query.trim() + "')");
                startActivity(intent);
                //防止数据两次加载
                sw_searchBox.setIconified(true);
                return false;
            }

            // 用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //给Toolbar的Menu设置点击事件
        tb_detailsIcon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    //点击开启无图模式
                    case R.id.open_no_graph_mode:
                        //清空Menu
                        tb_detailsIcon.getMenu().clear();
                        //重新给menu赋值
                        tb_detailsIcon.inflateMenu(R.menu.close_unmapped_mode_menu);
                        //设置isOpen的值
                        isOpen = true;
                        //webView重新加载页面
                        webViewLoadURL();
//                        wv_VideoDetails.loadUrl(getPage(isOpen, type));
                        Log.d("isOpen", isOpen + "");
                        break;
                    //点击关闭无图模式
                    case R.id.close_unmapped_mode:
                        //清空Menu
                        tb_detailsIcon.getMenu().clear();
                        //重新给menu赋值
                        tb_detailsIcon.inflateMenu(R.menu.open_no_graph_mode_menu);
                        //设置isOpen的值
                        isOpen = false;
                        //webView重新加载页面
                        webViewLoadURL();
//                        wv_VideoDetails.loadUrl(getPage(isOpen, type));
                        Log.d("isOpen", isOpen + "");
                        break;
                }
                return false;
            }
        });


        //默认选中视频  给视频、音频、文案、图片绑定单击事件
        tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空所有按钮选中状态
                setSelected();
                //将点击的按钮设置为选中状态
                tv_video.setSelected(true);
                //设置点击按钮的类型
                type = typeArray[0];
                //设置类型是否开启类目
                isOpenCategory = isOpenCategoryArray[0];
                //判断是否开启类目加载不同的页面
                webViewLoadURL();
//                wv_VideoDetails.loadUrl(getPage(isOpen, type));
                Log.d("必修URL", getPage(isOpen, type));
            }
        });
        tv_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_Image.setSelected(true);
                type = typeArray[1];
                isOpenCategory = isOpenCategoryArray[1];
                webViewLoadURL();
//                wv_VideoDetails.loadUrl(getPage(isOpen, type));
                Log.d("必修URL", getPage(isOpen, type));
            }
        });
        tv_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_frequency.setSelected(true);
                type = typeArray[2];
                isOpenCategory = isOpenCategoryArray[2];
                webViewLoadURL();
//                wv_VideoDetails.loadUrl(getPage(isOpen, type));
                Log.d("必修URL", getPage(isOpen, type));
            }
        });
        tv_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_pdf.setSelected(true);
                type = typeArray[3];
                isOpenCategory = isOpenCategoryArray[3];
                webViewLoadURL();
//                wv_VideoDetails.loadUrl(getPage(isOpen, type));
                Log.d("必修URL", getPage(isOpen, type));
            }
        });

        //给WebView设置基本属性
        NetWorkTools.XWalkView_Settings(wv_VideoDetails, this);
        wv_VideoDetails.setUIClient(new XWalkUIClient(wv_VideoDetails) {
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });
        //webView加载页面
        webViewLoadURL();
        Log.d("地址", getPage(isOpen, type));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_VideoDetails != null) {
            wv_VideoDetails.pauseTimers();
            wv_VideoDetails.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wv_VideoDetails != null) {
            wv_VideoDetails.resumeTimers();
            wv_VideoDetails.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_VideoDetails != null) {
            wv_VideoDetails.onDestroy();
        }
    }

    //初始化
    private void init() {
        //获取控件
        tb_detailsIcon = (Toolbar) findViewById(R.id.video_icon);
        tv_videoTitle = (TextView) findViewById(R.id.video_title);
        tv_video = (TextView) findViewById(R.id.tv_video);
        tv_frequency = (TextView) findViewById(R.id.tv_frequency);
        tv_pdf = (TextView) findViewById(R.id.tv_pdf);
        tv_Image = (TextView) findViewById(R.id.tv_Image);
        wv_VideoDetails = (XWalkView) findViewById(R.id.swipe_target);
//        scaleImageView = (ScaleImageView) findViewById(R.id.video_image);

        //获得viewPager对象
        viewPager = (ViewPager) findViewById(R.id.video_image);

        //初始化pagerAdapter
        pagerAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(myImageViews.get(position));
                return myImageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d("position", position + "");
                if (position < myImageViews.size()) {
                    container.removeView(myImageViews.get(position));
                }
            }

            @Override
            public int getCount() {
                return myImageViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };

        sw_searchBox = (SearchView) findViewById(R.id.details_search_box);

        sw_searchBox.setIconifiedByDefault(false);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        sw_searchBox.setSubmitButtonEnabled(true);//是否显示确认搜索按钮
        sw_searchBox.setQueryHint("请输入搜索关键字");

        //上拉加载布局设置监听
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
//        swipeToLoadLayout.setOnRefreshListener(this);
//        swipeToLoadLayout.setOnLoadMoreListener(this);


        //设置标题、设置返回按钮并添加监听事件
        intent = getIntent();
        //给名称、顺序、内容数量数组赋初值
        name = intent.getStringArrayExtra("name");
        //输出数组的值
        outputArray(name);
        order = intent.getStringArrayExtra("order");
        outputArray(order);
        number = intent.getStringArrayExtra("number");
        outputArray(number);
        reuse = intent.getStringExtra("reuse");
        Log.d("以上是得到的以下是排序后的", "------------------------");

        //给Toolbar设置标题
        tv_videoTitle.setText(intent.getStringExtra("title"));
        //给Toolbar设置返回图标
        tb_detailsIcon.setNavigationIcon(R.mipmap.arrow_left_d);
        //返回图标设置点击事件
        tb_detailsIcon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //调用IWXMsg.registerApp将应用注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);


        //读取本地文件中无图模式是否开启
        readSharedHelper();
        //判断是否开启无图模式 然后显示Menu
        if (isOpen) {
            tb_detailsIcon.inflateMenu(R.menu.close_unmapped_mode_menu);
        } else {
            tb_detailsIcon.inflateMenu(R.menu.open_no_graph_mode_menu);
        }

        //让类型、名称按照顺序数组排序
        typeArray = setArray(typeArray);
        outputArray(typeArray);
        name = setArray(name);
        outputArray(name);
        number = setArray(number);
        outputArray(number);
        //给是否开启类目数组赋值
        isOpenCategoryArray = intent.getStringArrayExtra("isOpenCategory");
        isOpenCategoryArray = setArray(isOpenCategoryArray);
        outputArray(isOpenCategoryArray);
        //给controlsArray赋初值
        controlsArray[0] = tv_video;
        controlsArray[1] = tv_Image;
        controlsArray[2] = tv_frequency;
        controlsArray[3] = tv_pdf;
        outputArray(controlsArray);

        //设置textView的值和显示与隐藏
        for (int i = 0; i < name.length; i++) {
            TextView textView = (TextView) controlsArray[i];
            textView.setText(name[i]);
            if (number[i].equals("0")) {
                textView.setVisibility(View.GONE);
            }
        }
        //根据textView是否隐藏设置第一个没隐藏的textView为选中状态并设置类型
        for (int i = 0; i < controlsArray.length; i++) {
            TextView textView = (TextView) controlsArray[i];
            int visibility = textView.getVisibility();
            if (visibility == 0) {
                textView.setSelected(true);
                type = typeArray[i];
                isOpenCategory = isOpenCategoryArray[i];
                break;
            }
        }
    }

    //根据顺序数组给名称、类型，数量排序
    private String[] setArray(String[] array) {
        String[] temporary = {"", "", "", ""};
        for (int i = 0; i < order.length; i++) {
            if (i != Integer.parseInt(order[i])) {
                temporary[Integer.parseInt(order[i])] = array[i];
            } else {
                temporary[i] = array[i];
            }
        }
        return temporary;
    }

    //输出数组
    private void outputArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            Log.d("必修数组的值", array[i] + "");
        }
        Log.d("分割线", "----------------------------");
    }

    //得到当前按钮被选中的URL地址
    private String getPage(Boolean isOpen, String type) {
        String page = "";
        if (isOpen) {
            switch (type) {
                case "avi":
                    page = MyApplication.url + "no_img_videoList.view?type=avi&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
                case "mp3":
                    page = MyApplication.url + "no_img_audio.view?type=mp3&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
                case "pdf":
                    page = MyApplication.url + "no_img_courseware.view?type=pdf&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
                case "png":
                    page = MyApplication.url + "no_img_picture.view?type=png&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
            }
        } else {
            switch (type) {
                case "avi":
                    page = MyApplication.url + "videoList.view?type=avi&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
                case "mp3":
                    page = MyApplication.url + "audio.view?type=mp3&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
                case "pdf":
                    page = MyApplication.url + "coursewareList.view?type=pdf&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
                case "png":
                    page = MyApplication.url + "picture.view?type=png&id=" + MyApplication.circleID + "&reuse=" + reuse + "&catalogueId=" + catalogueId;
                    break;
            }
        }

        return page;
    }

    //重置所有文本的选中状态
    private void setSelected() {
        tv_video.setSelected(false);
        tv_frequency.setSelected(false);
        tv_pdf.setSelected(false);
        tv_Image.setSelected(false);
    }

    //页面给原生传值
    @JavascriptInterface
    public void setValue(String string) {
        if (type.equals("pdf")) {
            intent = new Intent("android.intent.Activity.PDFActivity");
            intent.putExtra("pdf", string);
            startActivity(intent);
        } else if (type.equals("avi")) {
            intent = new Intent("android.intent.Activity.CoursewareDetailsActivity");
            intent.putExtra("courseware", string);
            startActivity(intent);
        } else if (type.equals("mp3")) {
            intent = new Intent("android.intent.Activity.AudioDetailsActivity");
            intent.putExtra("audio", string);
            startActivity(intent);
        }

    }


    //得到图片地址字符串数组
    @JavascriptInterface
    public void getImageUrlArray(String[] string) {
        /*bitmaps.clear();
        myImageViews.clear();
        for (int i = 0; i < string.length; i++) {
            Log.d("imgURL", string[i]);
//            bitmaps.add(getURLImage(string[i]));
            myImageView = new MyImageView(getApplicationContext());
            myImageView.setImageBitmap(getURLImage(string[i]));
            myImageViews.add(myImageView);
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
//                    Bitmap bmp = getURLImage(imgUrl);
                Message msg = new Message();
                msg.what = 0;
//                    msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();*/
       intent = new Intent("android.intent.Activity.ImageShowActivity");
       intent.putExtra("img_src",string);
       startActivity(intent);
    }

    //页面判断是否缴费
    @JavascriptInterface
    public void isVIP() {
        dialogs();
    }

    //得到类目ID
    @JavascriptInterface
    public void getCategoryID(String CategoryID) {
        catalogueId = CategoryID;
        //webView在另外的一个线程中使用loadUrl
        wv_VideoDetails.post(new Runnable() {
            @Override
            public void run() {
                wv_VideoDetails.loadUrl(getPage(isOpen, type));
                catalogueId = "";
            }
        });

    }

    //得到微信分享需要的 0:title，1:content，2:fileName
    @JavascriptInterface
    public void weChatShare(String[] shareArray) {
        Log.d("title", shareArray[0]);
        Log.d("content", NetWorkTools.replaceBlank(shareArray[1]));
        Log.d("fileName", shareArray[2]);
        Log.d("fileName", shareArray[2].substring(0, 13));
        //初始化一个WXWebPageObject对象，填写URL
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = MyApplication.url + "wx_share.view?fileName=" + shareArray[2] + "&id=" + shareArray[2].substring(0, 13) + "&userId=" + MyApplication.userName;
        Log.d("webpageUrl", webPage.webpageUrl);
        //用WXWebPageObject对象初始化一个WXMediaMessage对象，填写标题、描述
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = shareArray[0];
        msg.description = NetWorkTools.replaceBlank(shareArray[1]);
//        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
//        Bitmap bitmap=getURLImage(MyApplication.url + "upload/" + MyApplication.circleIcon);
        //微信将bitmap转换成byte[]
//        msg.thumbData=Util.bmpToByteArray(bitmap,true);
        //将bitmap转换成byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String imageType = MyApplication.circleIcon.substring(MyApplication.circleIcon.length() - 3);

        //判断图片类型 根据类型进行图片压缩 png或者jpg分享用圈子图片其他类型的用KMoon
        if (imageType.equals("png")) {
            Log.d("png", MyApplication.url + "upload/" + MyApplication.circleIcon);
            bitmapShare = getURLImage(MyApplication.url + "upload/" + MyApplication.circleIcon);
            //判断图片大小如果大于32kb进行最大模糊处理
            if (bitmapShare.getByteCount() > 32768) {
                bitmapShare.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
            } else {
                bitmapShare.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            }
        } else if (imageType.equals("jpg") || imageType.equals("jpeg")) {
            bitmapShare = getURLImage(MyApplication.url + "upload/" + MyApplication.circleIcon);
            if (bitmapShare.getByteCount() > 32768) {
                bitmapShare.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
            } else {
                bitmapShare.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }

        } else {
            Log.d("其他类型", MyApplication.url + "upload/" + MyApplication.circleIcon);
            bitmapShare = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            bitmapShare.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        }

        msg.thumbData = byteArrayOutputStream.toByteArray();
        //构造一个Rep
        req = new SendMessageToWX.Req();
//        req.transaction=buildTransaction("webPage");
        req.message = msg;
        showPopupWindow();
        //分享到好友会话
//        req.scene=SendMessageToWX.Req.WXSceneSession;
//        分享到朋友圈
//        req.scene=SendMessageToWX.Req.WXSceneTimeline;
//        api.sendReq(req);
    }

    //webView加载页面
    public void webViewLoadURL() {
        if (!isOpenCategory.equals("21")) {
            wv_VideoDetails.loadUrl(getPage(isOpen, type));
        } else {
            wv_VideoDetails.loadUrl(MyApplication.url + "category_list.view?circleId=" + MyApplication.circleID + "&S_CFSY=" + reuse + "&catalogueType=" + type);
            Log.d("类目地址", MyApplication.url + "category_list.view?circleId=" + MyApplication.circleID + "&S_CFSY=" + reuse + "&catalogueType=" + type);
        }
    }

    //将图片URL转换为Bitmap
    public Bitmap getURLImage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    //不是会员进行充值对话框
    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("您不是本圈子会员！请进行充值！");
        builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent("android.intent.Activity.WeChat_Alipay_pay");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    //详情对话框弹出主题和简介
    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    //按返回键事件
//    @Override
//    public void onBackPressed() {
//        int visibility = scaleImageView.getVisibility();
//        /*View.VISIBLE 常量值为0，意思是可见的
//        *View.INVISIBLE 常量值为4，意思是不可见的
//        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
//        * */
//        if (visibility == 8) {
//            finish();
//        } else if (visibility == 0) {
//            scaleImageView.setVisibility(View.GONE);
//        }
//    }
    //用户点返回键时直接关闭当前活动
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int visibility = viewPager.getVisibility();
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        wv_VideoDetails.getNavigationHistory().clear();
        if (visibility == 8 && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (visibility == 0 && keyCode == KeyEvent.KEYCODE_BACK) {
            viewPager.setVisibility(View.GONE);
        }
        return false;
    }

    //读取本地文件中无图模式是否开启
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getApplicationContext());
        Map<String, Object> data = sharedHelper.read();
        if (data.get("NoImages").toString().equals("true")) {
            isOpen = true;
        } else {
            isOpen = false;
        }
    }

    //显示PopupWindow
    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(VideoDetailsActivity.this).inflate(R.layout.popupwindow_layout, null);
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView pop_SceneSession = (TextView) contentView.findViewById(R.id.pop_SceneSession);
        TextView pop_SceneTimeline = (TextView) contentView.findViewById(R.id.pop_SceneTimeline);
        TextView pop_cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
        pop_SceneSession.setOnClickListener(this);
        pop_SceneTimeline.setOnClickListener(this);
        pop_cancel.setOnClickListener(this);
        //显示PopupWindow
        View rootView = LayoutInflater.from(VideoDetailsActivity.this).inflate(R.layout.popupwindow_layout, null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
    //上拉加载事件
    /*,OnLoadMoreListener
    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                wv_VideoDetails.loadUrl("javascript:loadMore(" + 2 + ")");
            }
        }, 500);
    }*/

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pop_SceneSession: {
//                Toast.makeText(this,"clicked computer",Toast.LENGTH_SHORT).show();
                //分享到好友会话
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_SceneTimeline: {
//                Toast.makeText(this,"clicked financial",Toast.LENGTH_SHORT).show();
                //        分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_cancel: {
//                Toast.makeText(this,"clicked manage",Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
            break;
        }
    }
}
