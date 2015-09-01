package com.example.administrator.ftm3.question;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicListCardProvider;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.example.administrator.ftm3.R;

import java.util.ArrayList;


/**
 * Created by Administrator on 2015/9/1 0001.
 */
public class QuestionsEnterActivity extends AppCompatActivity {

    private Context mContext;
    private MaterialListView mListView;
    private ArrayList<String> list ;
    private boolean islistview = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_enter);

        mContext = this;
        mListView = (MaterialListView) findViewById(R.id.material_listview);

        //增加listview
        ListViewCard();

        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                //短时间点击
                Log.e("MY", "短时间点击");

            }

            @Override
            public void onItemLongClick(Card card, int position) {
                //长时间点击
                Log.e("MY", "长时间点击");
            }
        });

        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int position) {
                // Do whatever you want here
                Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 问题与多个回答界面
     */
    public void ListViewCard(){

        //改变内容此处设置
        list = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            list.add("item" + i);
        }

        QuestionsEnterActivityAdapter adapter = new QuestionsEnterActivityAdapter(this,list);

        Card card = new Card.Builder(this)
                .setTag("LIST_CARD")
                .setDismissible()
                .withProvider(BasicListCardProvider.class)
                .setTitle("大家的回答")
                //.setDescription("Take a list")
                .setAdapter(adapter)
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("MY", "费尽千辛万苦终于成功监听");
                        EnterListViewCard();
                    }
                })
                .endConfig()
                .build();
        
        islistview = true;
        mListView.add(card);
    }

    /**
     * 个人回答
     */
    private void EnterListViewCard(){
        Card card1 = new Card.Builder(this)
                .setTag("SMALL_IMAGE_CARD")
                .setDismissible()
                .withProvider(SmallImageCardProvider.class)
                .setTitle("某某的回答")
                .setDescription("我的回答最精彩，谢谢！")
                .setDrawable(R.drawable.sample_android)
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
            Log.e("MY", "islistview:"+islistview);
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

}
