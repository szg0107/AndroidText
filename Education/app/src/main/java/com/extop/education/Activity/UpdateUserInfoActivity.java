package com.extop.education.Activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.Toast;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import com.extop.education.R;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Administrator on 2017/4/1.
 * 修改用户信息
 */

public class UpdateUserInfoActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "personalData.view?sys_bed=t", zhis="";
    private ProgressDialog pd;
    final Handler hand = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            //这里的话如果接受到信息码是123
            if(msg.what == 123)
            {
                pd.show();
            }else {
                pd.dismiss();
                Toast toast = Toast.makeText(UpdateUserInfoActivity.this, "上传成功！", Toast.LENGTH_LONG);
                // 这里给了一个1/4屏幕高度的y轴偏移量
                toast.setGravity(Gravity.TOP, 0, 300);
                toast.show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取控件
        tw_title.setText("个人信息");
        xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
        xWalkView.setUIClient(new UIClient(xWalkView));


        pd = new ProgressDialog(UpdateUserInfoActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        pd.setMessage("正在上传,请稍候...");// 设置ProgressDialog提示信息
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        pd.setIndeterminate(false);
        pd.setCancelable(false); // 设置ProgressDialog 是否可以按退回键取消
    }

    @JavascriptInterface
    public void setValue(String tishi, String id, String zhi) {
        Prompt(tishi, id, zhi);
    }

    @JavascriptInterface
    public void setValues(String baifenbi) {

        if (!baifenbi.equals("100%")){
            new Thread()
            {
                public void run()
                {
                    //把信息码发送给handle让更新界面
                    hand.sendEmptyMessage(123);
                }
            }.start();
        }else {
            new Thread()
            {
                public void run()
                {
                    //把信息码发送给handle让更新界面
                    hand.sendEmptyMessage(0);
                }
            }.start();
        }

    }
    //填写信息对话框
    protected void Prompt(final String tishi, final String id, final String zhi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserInfoActivity.this, R.style.AlertDialog);
        final EditText et = new EditText(UpdateUserInfoActivity.this);
        et.setSingleLine();
        et.setWidth(800);
        et.setHeight(150);
        et.setBackgroundResource(R.drawable.alert_edittext_style);
        et.setPadding(50, 0, 0, 0);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (zhi.length() != 0) {
                    et.setText(zhi);
                    et.setSelection(et.getText().length());
                } else {
                    et.setHint(tishi);
                }
            }
        });
        builder.setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //解决webView调用JS出错
                        zhis = et.getText().toString().trim();
                        xWalkView.post(new Runnable() {
                            @Override
                            public void run() {
                                xWalkView.loadUrl("javascript:fuzhi('" + id + "','" + zhis + "')");
                            }
                        });
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //确认对话框
    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserInfoActivity.this, R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }





    private ValueCallback<Uri> mFilePathCallback;
    private String mCameraPhotoPath;
    public static final int INPUT_FILE_REQUEST_CODE = 1;

    class UIClient extends XWalkUIClient {

        public UIClient(XWalkView view) {
            super(view);
        }

        public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile,
                                    String acceptType, String capture) {
            if(mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = uploadFile;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null){
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("WebViewSetting", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            /*File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                Log.e("错误", "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }


            Intent contentSelectionIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);*/

        }

        @Override
        public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
            dialog(message);
            result.cancel();
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            xWalkView.onActivityResult(requestCode, resultCode, data);
            return;
        }


        Uri results = null;

        // Check that the response is a good one
        if(resultCode == Activity.RESULT_OK) {
            if(data == null) {
                // If there is not data, then we may have taken a photo
                if(mCameraPhotoPath != null) {
                    results = Uri.parse(mCameraPhotoPath);
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = Uri.parse(dataString);
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }
}
