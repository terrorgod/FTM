package com.example.administrator.ftm3.ask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ftm3.MainActivity;
import com.example.administrator.ftm3.R;
import com.example.administrator.ftm3.question.Frame_questions;
import com.example.administrator.ftm3.question.QuestionAndDescribe;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Administrator on 2015/8/30 0030.
 */
public class AskAcitivity extends FragmentActivity implements View.OnClickListener{

    private TextView tv_table_question, tv__table_describe;
    private ArrayList<Fragment> listFragment;
    private ViewPager vp_fragment;
    // 指示器
    private ImageView iv_cursor;
    // 记录当前选中的tab的index
    private int currentIndex = 0;
    // 指示器的偏移量
    private int offset = 0;
    private int leftMargin=0;
    // 屏幕宽度
    private int screenWidth = 0;
    // 屏幕宽度的二分之一
    private int screen1_2;

    private LinearLayout.LayoutParams lp;
    private final int Code_Success = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Code_Success){
                Toast.makeText(AskAcitivity.this,"发表成功",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        init();
    }

    /**
     *发表按钮监听
     */
    public void Upload(View v){
        //弹框询问
        Upload_Pop_Up();
    }

    /**
     *取消按钮监听
     */
    public void Back(View v){
        //返回界面
        finish();
    }

    /**
     * 内容为空弹框提示
     */
    public void Null_Pop_Up(){
        new AlertDialog.Builder(this).setTitle("空内容提示")
                .setMessage("请确定在问题栏以及学号处输入内容")
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 发表按钮弹框提示
     */
    public void Upload_Pop_Up(){
        new AlertDialog.Builder(this).setTitle("发表提示")//设置对话框标题

                .setMessage("确定发表吗？")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        //这里是发表设置，需要与服务器交互，提交数据
                        EditText et_ask_question = (EditText) findViewById(R.id.et_ask_question);
                        EditText et_describe = (EditText) findViewById(R.id.et_describe);
                        EditText et_activity_studentID = (EditText) findViewById
                                (R.id.et_activity_studentID);
                        //判断是否为空
                        if (TextUtils.isEmpty(et_ask_question.getText()) ) {
                            //内容为空弹框提示
                            Null_Pop_Up();
                        } else {
                            String question = et_ask_question.getText().toString();
                            String describe = et_describe.getText().toString();
                            String studentID = et_activity_studentID.getText().toString();

                            Log.e("MY", "TESTdescribe:"+describe);
                            //通过get提交数据
                            SubMitData(question,describe,studentID);
                        }

                        //获取当前系统时间
                        //CurrentTime();
                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                Log.e("MY", " 已点击取消，不发表提问");
            }

        }).show();//在按键响应事件中显示此对话框
    }

    /**
     *获取当前系统时间
     */
    public String CurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        Log.e("MY", str);
        return str;
    }

    /**
     * 布局初始化
     */
    private void init(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screen1_2 = screenWidth / 2;

        iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
        lp = (LinearLayout.LayoutParams) iv_cursor.getLayoutParams();
        leftMargin = lp.leftMargin;

        tv_table_question = (TextView) findViewById(R.id.tv_table_question);
        tv__table_describe = (TextView) findViewById(R.id.tv_table_describe);
        tv_table_question.setOnClickListener(this);
        tv__table_describe.setOnClickListener(this);
        setViewPager();
    }

    /**
     * 初始化ViewPager布局
     */
    private void setViewPager(){
        vp_fragment = (ViewPager) findViewById(R.id.vp_fragment);
        listFragment = new ArrayList<>();
        Frame_ask_question frame_read= new Frame_ask_question();
        listFragment.add(frame_read);
        Frame_describe frame_write = new Frame_describe();
        listFragment.add(frame_write);
        vp_fragment.setAdapter(new FragmentAdapter(getSupportFragmentManager(), listFragment));
        vp_fragment.setCurrentItem(0);
        vp_fragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                offset = (screen1_2 - iv_cursor.getLayoutParams().width) / 2;
                final float scale = getResources().getDisplayMetrics().density;
                if (position == 0) {// 0<->1
                    lp.leftMargin = (int) (positionOffsetPixels / 2) + offset;
                } else if (position == 1) {// 1<->2
                    lp.leftMargin = (int) (positionOffsetPixels / 2) + screen1_2 + offset;
                }
                iv_cursor.setLayoutParams(lp);
                currentIndex = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * get提交数据
     */
    public void SubMitData(final String question,final String describe,final String studentID )  {
                    Log.e("MY", "准备提交问题的所有数据");
                    Thread t = new Thread(){
                        @Override
                        public void run() {
                            String path = "http://120.24.242.211/tushu/questions.php"+
                                    "?question=" +URLEncoder.encode(question)+"&describe="+
                                    URLEncoder.encode(describe)+ "&studentID="+
                                    URLEncoder.encode(studentID);
                            //String text;
                            try {
                                URL url = new URL(path);
                                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setReadTimeout(5000);
                                conn.setConnectTimeout(5000);

                    if(conn.getResponseCode() == 200){
                        InputStream is = conn.getInputStream();
                        byte[] b = new byte[1024];
                        int len = 0;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = is.read(b))!= -1){
                            baos.write(b,0,len);
                        }

                        String text = new String(baos.toByteArray());
                        Log.e("MY", "所提交的问题的xml内容查看："+text);
                        //发送消息，吐司弹框
                        Message message = Message.obtain();
                        message.what = Code_Success;
                        handler.sendMessage(message);

                        //最好在服务器判断学号正确性，然后再返回数据是否成功
                        Log.e("MY", "成功提交问题的数据");

                        //退出提问界面
                        finish();
                    }
                } catch (Exception e) {

                    Toast.makeText(AskAcitivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                    Log.e("MY", "提交问题的数据失败");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    /**
     *对两个tab的监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_table_question:
                vp_fragment.setCurrentItem(0);
                break;
            case R.id.tv_table_describe:
                vp_fragment.setCurrentItem(1);
                break;
        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> list;
        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }
        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
        @Override
        public int getCount() {
            return list.size();
        }

    }
}
