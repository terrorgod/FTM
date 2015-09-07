package com.example.administrator.ftm3.question;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ftm3.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/1 0001.
 */
public class QuestionsEnterActivityAdapter extends BaseAdapter {

    private ArrayList<RespondAndStudentID> list;
    private Context context;

    public QuestionsEnterActivityAdapter(Context context, ArrayList<RespondAndStudentID> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        ;
        if (convertView == null) {
            //这里修改布局
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_listview,
                    null);
            //这里修改组件
            holder.tv_activity_name = (TextView) convertView
                    .findViewById(R.id.tv_activity_name);
            holder.tv_activity_content = (TextView) convertView
                    .findViewById(R.id.tv_activity_content);
            holder.tv_activity_time = (TextView) convertView
                    .findViewById(R.id.tv_activity_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //改变数据 参数类型：ArrayList<RespondAndStudentID>
        holder.tv_activity_name.setText(list.get(position).getStudentID());
        holder.tv_activity_content.setText(list.get(position).getRespond());
        holder.tv_activity_time.setText(list.get(position).getDate());
        return convertView;
    }

    class ViewHolder {
        TextView tv_activity_name;
        TextView tv_activity_content;
        TextView tv_activity_time;
    }

    //弹框提示
   /* public void Pop_up(final int position){
        new AlertDialog.Builder(context).setTitle("删除提示")//设置对话框标题

                .setMessage("确定删除吗？")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        //这里是删除键设置，需要与服务器交互，提交删除数据
                        list.remove(position);
                        notifyDataSetChanged();
                    }

                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

                Log.i("MY", " 不删除哦");
            }

        }).show();//在按键响应事件中显示此对话框
    }*/
}
