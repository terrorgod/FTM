package com.example.administrator.ftm3.download;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/8/9 0009.
 */
public class Thread_Downlaod {

    //注意：需要设置权限
    //1.读取sd卡权限
    //2.internet权限

    //第一参数为下载路径，第二个参数为线程数
    public static void FileDownload(final String path ,final int threadNum) {

        Thread t = new Thread(){
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(3000);
                    conn.setConnectTimeout(3000);

                    Log.e("MY", "connect");
                    if (conn.getResponseCode() == 200) {
                        long length = conn.getContentLength();
                        long size = length / threadNum;
                        for (int i = 0; i < threadNum; i++) {
                            long startIndex = i * size;
                            long endIndex = (i + 1) * size;

                            if (i == threadNum - 1) {
                                endIndex = length - 1;
                            }

                            Log.e("MY", "子线程下载");
                            new Mythread(startIndex, endIndex, path).start();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }

    //第一参数为下载路径，第二个参数为线程数,第三个参数为进度条弹框
    public static File FileDownload(final String path ,final int threadNum, final ProgressDialog progressDialog) {

                       URL url;
                       File file = null;
                       try {
                           //第一个参数为存放位置（此处为sd卡），第二个参数为文件名
                           file = new File(Environment.getExternalStorageDirectory(),Spilt(path));

                           url = new URL(path);
                           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                           conn.setRequestMethod("GET");
                           conn.setReadTimeout(3000);
                           conn.setConnectTimeout(3000);

                           Log.e("MY", "path:"+path);
                           Log.e("MY", "connect");
                           if (conn.getResponseCode() == 200) {
                               int length = conn.getContentLength();

                               //设置进度条最大值
                               progressDialog.setMax(length);

                               long size = length / threadNum;
                               for (int i = 0; i < threadNum; i++) {
                                   long startIndex = i * size;
                                   long endIndex = (i + 1) * size;

                                   if (i == threadNum - 1) {
                                       endIndex = length - 1;
                                   }

                                   Log.e("MY", "子线程下载");
                                   new Mythread(startIndex, endIndex, path, progressDialog,file).start();
                               }
                           }

                       } catch (Exception e) {
                           e.printStackTrace();
                       }
        return file;
    }


    //文件名
    public static String Spilt(String path){
        int idx = path.lastIndexOf("/");
        return path.substring(idx + 1, path.length());
    }
}