package com.example.administrator.ftm3.question;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.administrator.ftm3.MainActivity;
import com.example.administrator.ftm3.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/8 0008.
 */
public class Frame_questions extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    private Frame_questions_adapter adapter;
    private ArrayList<String> list ;


    //侧滑栏所用
    private static final String ARG_SECTION_NUMBER = "section_number";

    //刷新
    private SwipeRefreshLayout swipeLayout;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<String>();

        //改变内容此处设置
        for (int i = 0; i < 30; i++) {
            list.add("item" + i);
        }


        adapter = new Frame_questions_adapter(getActivity(), list);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_questions,null);
        setListAdapter(adapter);



        //刷新
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        //第一个参数android.R.color.holo_blue_bright，换个颜色而已
        swipeLayout.setColorScheme(
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return v;
    }

    //对listview的监听
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //转至具体问题应该携带数据，这里做了参考
        Intent intent =new Intent(getActivity(),QuestionsEnterActivity.class);
        //intent.putExtra("name",list.get(position));
        //intent.putExtra("content",list.get(position));
        startActivity(intent);

        Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        Log.e("MY", "Refresh");
        //重新加载数据(未完善)

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



