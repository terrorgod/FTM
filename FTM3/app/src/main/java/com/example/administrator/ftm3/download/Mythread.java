package com.example.administrator.ftm3.download;

import android.app.ProgressDialog;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/8/9 0009.
 */
public class Mythread extends Thread{

    private long startIndex;
    private long endIndex;
    private String path;
    private ProgressDialog progressDialog;

    static int current;

    private File file;


    public Mythread(long startIndex, long endIndex, String path, ProgressDialog progressDialog, File file) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.path = path;
        this.progressDialog = progressDialog;
        this.file = file;
    }

    public Mythread(long startIndex, long endIndex, String path) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
            if(conn.getResponseCode() == 206) {
                InputStream is = conn.getInputStream();
                RandomAccessFile raf = new RandomAccessFile(file,"rwd");

                raf.seek(startIndex);

                byte[] b =new byte[1024];
                int len = 0;
                int total =0;
                while((len=is.read(b))!=-1) {
                    raf.write(b,0,len);
                    total+=len;
                    current += len;

                    //设置进度条当前值
                    progressDialog.setProgress(current);
                }
                Log.e("MY", "本次APK下载了："+total);
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
