package com.example.administrator.ftm3.question;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicListCardProvider;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.example.administrator.ftm3.R;


import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by Administrator on 2015/9/1 0001.
 */
public class QuestionsEnterActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Context mContext;
    private MaterialListView mListView;
    private boolean islistview = true;
    private TextView tv_activity_question;
    private ArrayList<RespondAndStudentID>  list;
    private String questionID;
    private QuestionsEnterActivityAdapter adapter;
    private String path;
    private String next;

    //刷新
    private SwipeRefreshLayout swipeLayout;

    /**
     * 刷新UI
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Log.e("MY", "开始匹配以及填充回答的listview的数据");
            //增加listview
            ListViewCard();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_enter);

        mContext = this;
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        list = new ArrayList<RespondAndStudentID>();
        questionID = (String)getIntent().getExtras().get("questionID");
        path = "http://120.24.242.211/tushu/getanswers.php"+ "?questionid="+URLEncoder.encode(questionID);

        //获取数据
        GetData();

        //设置刷新
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_activity);
        swipeLayout.setOnRefreshListener(this);
        //第一个参数android.R.color.holo_blue_bright，换个颜色而已
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );



        //textview设置内容
        tv_activity_question = (TextView) findViewById(R.id.tv_activity_question);
        tv_activity_question.setText((String) getIntent().getExtras().get("question"));






        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                //短时间点击
                Log.e("MY", "你已短时间点击该View");

            }

            @Override
            public void onItemLongClick(Card card, int position) {
                //长时间点击
                Log.e("MY", "你已长时间点击该View");
            }
        });

        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int position) {
                // Do whatever you want here
                //Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();

                Log.e("MY", "本次获得answerid值为:" + next);
                //右滑监听,加载数据

                if(next.equals("0")){
                    LoadDateCard();
                }else {
                    Log.e("MY", "提交增加answerid");
                    mListView.clear();
                    path = "http://120.24.242.211/tushu/getanswers.php"+ "?questionid="+URLEncoder.encode(questionID)+"&answerid="+URLEncoder.encode(next);
                    GetData();
                }

            }
        });
    }

    /**
     * 获得数据
     */
    public void GetData(){
        Log.e("MY", "准备下载含对应于questionid的回答的xml文件");
        Thread t = new Thread(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(path);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);

                    Log.e("MY", "请求获得服务器响应");
                    if(conn.getResponseCode() == 200){
                        InputStream is = conn.getInputStream();

                        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int len = 0;
                        byte[] b = new byte[1024];
                        while ((len = is.read(b))!=-1){
                            baos.write(b,0,len);
                        }
                        String text = new String(baos.toByteArray());
                        Log.e("MY", "xml内容："+text);
                        Log.e("MY", "下载完成，准备解析xml");*/
                        Log.e("MY", "下载完成，准备解析含有对应于questionid的数据的xml");
                        //解析xml
                        ParseXML(is);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    /**
     *解析xml文件
     */
    public void ParseXML(InputStream is){
        Log.e("MY", "开始解析含有对应于questionid的回答的xml");
        XmlPullParser xp = Xml.newPullParser();
        try {
            xp.setInput(is,"utf-8");

            //判断节点事件类型
            int type = xp.getEventType();

            RespondAndStudentID ras =null;
            while (type != XmlPullParser.END_DOCUMENT){
                switch (type){
                    case XmlPullParser.START_TAG:
                        if("respondall".equals(xp.getName())){
                            ras= new RespondAndStudentID();
                        }else if ("questionid".equals(xp.getName())) {
                            String questionID = xp.nextText();
                            ras.setQuestionID(questionID);
                            Log.e("MY", "questionID:"+ras.getQuestionID());
                        }else if ("date".equals(xp.getName())){
                            String date = xp.nextText();
                            ras.setDate(date);
                            Log.e("MY", "data:"+ras.getDate());
                        } else if("studentID".equals(xp.getName())){
                            String studentID = xp.nextText();
                            ras.setStudentID(studentID);
                            Log.e("MY", "studentID:"+ras.getStudentID());
                        }else if("respond".equals(xp.getName())){
                            String respond = xp.nextText();
                            ras.setRespond(respond);
                            Log.e("MY", "respond:"+ras.getRespond());
                        }else if("next".equals(xp.getName())){
                            next = xp.nextText();
                            Log.e("MY", "answerid:"+next);
                        }
                        break;
                    case  XmlPullParser.END_TAG:
                        if("respondall".equals(xp.getName())){
                            list.add(ras);
                        }
                        break;
                }

                //解析完当前节点，指针移至下一个节点
                type= xp.next();
            }

            //打印测试一番
            for(RespondAndStudentID r: list){
                Log.e("MY", "xml解析得到的list所含有对应于questionid的回答"+r.toString());
            }

            Log.e("MY", "含有对应于questionid的回答的解析xml完毕");

            //发送消息,刷新ui
            handler.sendEmptyMessage(1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *监听我要回答按钮
     */
    public void Respond(View v){
        //跳转至回答界面
        Intent intent = new Intent(this,RespondActivity.class);
        intent.putExtra("questionID",questionID);
        startActivity(intent);
    }

    /**
     * 问题与多个回答界面
     */
    public void ListViewCard(){

        //改变内容此处设置


       /* list = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            list.add("item" + i);
        }*/

        adapter = new QuestionsEnterActivityAdapter(this,list);

        Card card = new Card.Builder(this)
                .setTag("LIST_CARD")
                .setDismissible()
                .withProvider(BasicListCardProvider.class)
                .setTitle("大家的回答")
                //.setDescription("点击得到更多人回答")
                .setAdapter(adapter)
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("MY", "费尽千辛万苦终于成功监听");
                        String respond = list.get(position).getRespond();
                        String studentID = list.get(position).getStudentID();
                        String date = list.get(position).getDate();
                        EnterListViewCard(studentID , respond , date);
                    }
                })
                .endConfig()
                .build();


        islistview = true;
        mListView.add(card);
        //增加加载数据卡片
        LoadDateCard();
    }

    /**
     * 加载数据卡片
     */
    private void LoadDateCard(){
            Card card = new Card.Builder(this)
                    .setTag("SMALL_IMAGE_CARD")
                    .setDismissible()
                    .withProvider(SmallImageCardProvider.class)
                    //.setTitle("B卡片")
                    .setDescription("    向右滑动得到更多回答")
                    //.setDrawable(R.drawable.sample_android)
                    .endConfig()
                    .build();
            mListView.add(card);
    }

    /**
     * 个人回答
     */
    private void EnterListViewCard(String studentID , String respond , String date){
        Card card1 = new Card.Builder(this)
                .setTag("SMALL_IMAGE_CARD")
                .setDismissible()
                .withProvider(SmallImageCardProvider.class)
                .setTitle(studentID)
                .setDescription(respond)
                //.setDrawable()
                .endConfig()
                .build();

        //切换卡片
        mListView.clear();
        islistview =false;
        mListView.add(card1);
    }



    /**
     *监听返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(islistview == true){
                finish();
            }
            else{
                //切换卡片
                mListView.clear();
                //重新进入listview
                ListViewCard();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

        @Override
        public void onRefresh() {
            //重新加载数据
            if(islistview == true){
                Log.e("MY", "对应于questionid的所有回答界面刷新");
                //清理卡片
                mListView.clear();
                //清空list列表
                list.clear();
                GetData();
            }
            else {
                Log.e("MY", "某个人的回答刷新");
                //不做更新，就给他们看看吧。。
            }
            swipeLayout.setRefreshing(false);
        }

}
