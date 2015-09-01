package com.example.administrator.ftm3.ask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ftm3.R;

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
        //内容为空弹框提示
        Null_Pop_Up();
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

                        //获取当前系统时间
                        CurrentTime();
                    }

                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                Log.e("MY", " 不发表哦");
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
