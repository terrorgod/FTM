package com.example.administrator.ftm3.question;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ftm3.R;

import java.util.ArrayList;

public class Frame_questions_adapter extends BaseAdapter {
   private ArrayList<QuestionAndDescribe> list;
   private Context context;

   public Frame_questions_adapter(Context context, ArrayList<QuestionAndDescribe> list) {
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
           convertView = LayoutInflater.from(context).inflate(R.layout.fragment_listview,
                   null);
           holder.tv_fragment_question = (TextView) convertView
                   .findViewById(R.id.tv_fragment_question);
           holder.tv_fragment_content = (TextView) convertView
                   .findViewById(R.id.tv_fragment_content);
           holder.tv_fragment_time = (TextView) convertView
                   .findViewById(R.id.tv_fragment_time);

           convertView.setTag(holder);
       } else {
           holder = (ViewHolder) convertView.getTag();
       }
       //改变数据 参数类型：ArrayList<QuestionAndDescribe>
       QuestionAndDescribe qad = list.get(position);
       holder.tv_fragment_question.setText(qad.getQuestion());
       holder.tv_fragment_content.setText(qad.getDescribe());
       holder.tv_fragment_time.setText(qad.getDate());
      /* holder.iv_delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //删除弹框提示
               Pop_up(position);
           }
       });*/

       return convertView;
   }

   class ViewHolder {
       TextView tv_fragment_question;
       TextView tv_fragment_content;
       TextView tv_fragment_time;
   }

    /*//删除弹框提示
    public void Pop_up(final int position){
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
