package com.example.administrator.ftm3.question;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.administrator.ftm3.MainActivity;
import com.example.administrator.ftm3.R;

import org.xmlpull.v1.XmlPullParser;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/8 0008.
 */
public class Frame_questions extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    private Frame_questions_adapter adapter;
    private ArrayList<QuestionAndDescribe>  list;
    private ListView mlistview;
    private String path;
    private String next;
    private final int Code_listview = 1;
    private Message message;

    //侧滑栏所用
    private static final String ARG_SECTION_NUMBER = "section_number";

    //刷新
    private SwipeRefreshLayout swipeLayout;

    //上拉加载
    private FloatingActionButton floatingActionButton;

    //侧滑栏所用
    public Frame_questions(){
    }

    //侧滑栏所用
    public static Frame_questions newInstance(int sectionNumber) {
        Frame_questions fragment = new Frame_questions();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 刷新UI
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    Log.e("MY", "开始匹配以及填充问题的listview的数据");
                    //设置listview适配器
                    adapter = new Frame_questions_adapter(getActivity(), list);
                    setListAdapter(adapter);
                    break;
            }


        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 获得数据 (这里是不是得缓存，不然每次刷新都下载有点问题吧)
     */
    public void GetData(){
        Log.e("MY", "准备下载含有所有问题数据的xml文件");
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

                    if(conn.getResponseCode() == 200){
                        InputStream is = conn.getInputStream();

                       /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int len = 0;
                        byte[] b = new byte[1024];
                        while ((len = is.read(b))!=-1){
                            baos.write(b,0,len);
                        }
                        String text = new String(baos.toByteArray());
                        Log.e("MY", "xml内容："+text);*/
                        Log.e("MY", "下载完成，准备解析含有所有问题数据的xml");
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
     *解析XML
     */
    public void ParseXML(InputStream is){
        Log.e("MY", "开始解析含有所有问题数据的xml");
        XmlPullParser xp = Xml.newPullParser();
        try {
            xp.setInput(is,"utf-8");

            //判断节点事件类型
            int type = xp.getEventType();

            QuestionAndDescribe qad = null;

            while (type != XmlPullParser.END_DOCUMENT){

                switch (type){
                    case XmlPullParser.START_TAG:
                        if("ask".equals(xp.getName())){

                        }else if("questionall".equals(xp.getName())){
                            qad= new QuestionAndDescribe();
                        }else if("studentID".equals(xp.getName())){
                            String studentID = xp.nextText();
                            qad.setStudentID(studentID);
                        } else if ("questionid".equals(xp.getName())) {
                            String questionID = xp.nextText();
                            qad.setQuestionID(questionID);
                        }else if("date".equals(xp.getName())){
                            String date = xp.nextText();
                            qad.setDate(date);
                        }else if ("n_answer".equals(xp.getName())){
                            String n_answer = xp.nextText();
                            qad.setN_answer(n_answer);
                        } else if("question".equals(xp.getName())){
                            String question = xp.nextText();
                            Log.e("MY", "question:"+question);
                            qad.setQuestion(question);
                        }else if("describe".equals(xp.getName())) {
                            String describe = xp.nextText();
                            Log.e("MY", "describe:" + describe);
                            qad.setDescribe(describe);
                        }else if("next".equals(xp.getName())){
                            next = xp.nextText();
                            Log.e("MY", "next:" + next);
                        }
                        break;
                    case  XmlPullParser.END_TAG:
                        if("questionall".equals(xp.getName())){
                            Log.e("MY", "qad"+qad.toString());
                            list.add(qad);
                        }
                        break;
                }

                //解析完当前节点，指针移至下一个节点
                type= xp.next();
            }

            for(QuestionAndDescribe q : list){
                Log.e("MY", "xml解析得到的list所含有的问题数据"+q.toString());
            }

            Log.e("MY", "解析含有所有问题数据的xml完毕");

            //发送消息,刷新ui
            message = Message.obtain();
            message.what = Code_listview;
            handler.sendMessage(message);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_questions,null);
        path = "http://120.24.242.211/tushu/getquestions.php";
        list = new ArrayList<QuestionAndDescribe>();
        mlistview = (ListView) v.findViewById(android.R.id.list);

        //获得数据,并解析xml，再listview适配器，它在handler中刷新ui，因为有线程
        GetData();

        //刷新
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        //第一个参数android.R.color.holo_blue_bright，换个颜色而已
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        //上拉加载
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fabbutton);
        floatingActionButton.listenTo(mlistview);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MY", "开始上拉加载");
                //加载数据
                //每次加载20个数据
                Log.e("MY", "本次获得的next值为：" + next);
                path = "http://120.24.242.211/tushu/getquestions.php"+"?id="+
                        URLEncoder.encode(next);
                //未解决：每次加载后都会跳至listview最上方，第一：不能在getdata后面设置，有线程，
                // 第二：在handler中出现严重卡顿
                GetData();

            }
        });

        return v;
    }


    //对listview的监听
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);

        //转至具体问题应该携带数据，这里做了参考
        Intent intent =new Intent(getActivity(),QuestionsEnterActivity.class);
        intent.putExtra("question", list.get(position).getQuestion());
        intent.putExtra("questionID", list.get(position).getQuestionID());
        startActivity(intent);

       // Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        Log.e("MY", "刷新，重新加载文件");
        //重新加载数据
        //清空list列表
        list.clear();
        path = "http://120.24.242.211/tushu/getquestions.php";
        GetData();

        swipeLayout.setRefreshing(false);

    }

    /**
     *侧滑栏所用
     */
    //onAttach中通过onSectionAttached取得ARGSECTIONNUMBER对应的值并设置为title
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}



