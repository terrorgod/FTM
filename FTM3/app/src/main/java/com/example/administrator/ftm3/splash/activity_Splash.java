package com.example.administrator.ftm3.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ftm3.MainActivity;
import com.example.administrator.ftm3.R;
import com.example.administrator.ftm3.download.Thread_Downlaod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2015/8/11 0011.
 */
public class activity_Splash extends Activity {
    private TextView tv_version;
    String path = "http://120.24.242.211/tushu/update/update.json";

    private String mversionName;
    private String mdescription;
    private int mversionCode;
    private String mdownload;

    private final int Code_Dialog = 1;
    private final int Code_Error_URL = 2;
    private final int Code_Error_Internet = 3;
    private final int Code_Error_JSON = 4;


    ProgressDialog progressDialog;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case Code_Dialog:
                    dialog();
                    break;
                case Code_Error_JSON:
                    Toast.makeText(activity_Splash.this,"JSON解析失败",Toast.LENGTH_SHORT).show();
                    break;
                case Code_Error_Internet:
                    Toast.makeText(activity_Splash.this,"网络错误",Toast.LENGTH_SHORT).show();
                    break;
                case Code_Error_URL:
                    Toast.makeText(activity_Splash.this,"URL异常",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv_version = (TextView) findViewById(R.id.tv_version);

        //获得本地版本名称
        tv_version.setText("版本号："+GetVersionName());

        //检测服务器版本号是否需要更新
        CheckVersion();
    }

    /**
     *获取本地版本名称
     */
    private String GetVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            Log.e("MY", "GetVersionCode " + versionCode);
            Log.e("MY", "GetVersionName " + versionName);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *获取本地版本号
     */
    private int GetVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            Log.e("MY", "GetVersionCode " + versionCode);
            Log.e("MY", "GetVersionName " + versionName);

            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     *检查服务器版本号与本地版本号是否匹配
     */
    private void CheckVersion() {

        final long startTime = System.currentTimeMillis();

        Thread t = new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                URL url ;
                HttpURLConnection conn = null;
                try {
                    url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);

                    Log.e("MY", "CheckVersion "+"准备连接");
                    if(conn.getResponseCode() == 200) {

                        Log.e("MY", "CheckVersion "+"进入连接");
                        InputStream is = conn.getInputStream();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        byte[] b = new byte[1024];
                        int len = 0;

                        while ((len = is.read(b)) != -1) {
                            baos.write(b, 0, len);
                        }

                        String content = baos.toString();

                        Log.e("MY", "CheckVersion "+content);

                        is.close();
                        baos.close();

                        //解析JSON
                        analysis_JSON(content,message);

                        //校验版本信息
                        if (GetVersionCode() < mversionCode) {

                            Log.e("MY", "校验弹框");
                            //弹出升级框
                            message.what = Code_Dialog;
                            handler.sendMessage(message);

                        } else {

                            long  endTime = System.currentTimeMillis();
                            long useTime = endTime - startTime;
                            //保证闪屏页时间为5秒

                            if(useTime < 5000){
                                Log.e("MY", "Time进入");
                                try {
                                    Thread.sleep(5000 - useTime);
                                    Log.e("MY", "Time结束");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //进入主界面
                            skipMainActivity();
                        }
                    }
                } catch (MalformedURLException e) {
                    //URL异常
                    message.what =Code_Error_URL;
                    handler.sendMessage(message);
                    skipMainActivity();
                    e.printStackTrace();
                }
                catch (IOException e) {
                    //网络异常
                    message.what =Code_Error_Internet;
                    handler.sendMessage(message);
                    skipMainActivity();
                    e.printStackTrace();
                }
                finally {
                    if(conn!=null){
                        conn.disconnect();
                    }

                }
            }
        };
        t.start();
    }

    /**
     *解析JSON
     */
    private void analysis_JSON(String content,Message message){
        try {
            Log.e("MY", "analysis_JSON 初始化");
            JSONObject json = new JSONObject(content);

            mversionName = json.getString("versionName");
            mdescription = json.getString("description");
            mversionCode = json.getInt("versionCode");
            mdownload = json.getString("downloadUrl");
            Log.e("MY", "analysis_JSON 初始化结束");
        } catch (JSONException e) {
            //JSON异常
            Log.e("MY", "analysis_JSON 异常");
            message.what =Code_Error_JSON;
            handler.sendMessage(message);
            skipMainActivity();
            e.printStackTrace();
        }
    }


    /**
     * 升级对话框
     */
    private void dialog(){
        Log.e("MY", "进入升级对话框");
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("最新版本" + mversionName);
        build.setMessage(mdescription);
        build.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载资源
                Log.e("MY", "准备下载apk");
                APKDownload();
            }
        });

        build.setNegativeButton("等会更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳到主界面
                skipMainActivity();
            }
        });

        build.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //返回键触发跳转主界面
                skipMainActivity();
            }
        });

        build.show(); // 弹框
    }

    /**
     * 跳转主界面
     */
    private void skipMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        //防止退出时进入splash界面或安装界面
        finish();
    }

    /**
     * 下载更新文件
     */
    private void APKDownload(){

        //通过进度条弹框显示进度
        Log.e("MY", "准备进度条弹框");
        progressDialog = new  ProgressDialog(activity_Splash.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载更新");
        progressDialog.show();

        Thread t = new Thread(){
            @Override
            public void run() {

                //下载apk
                Log.e("MY", "准备下载");
                File file = Thread_Downlaod.FileDownload(mdownload, 3, progressDialog);
                Log.e("MY", "下载完毕");

                try {
                    //这里下载过快，先让它睡一会
                    sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //安装apk
                installApk(file);
                Log.e("MY", "安装完毕");
                //结束进度条
                progressDialog.dismiss();

            }
        };
        t.start();

    }

    /**
     *安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);

        //安装后点打开会打开新版本
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        //安装时点返回键处理
        startActivityForResult(intent, 0);

        //提示完成和打开
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     *对安装返回键处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回主界面
        skipMainActivity();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
