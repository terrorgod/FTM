package com.example.administrator.ftm3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicImageButtonsCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;

/**
 * Created by Administrator on 2015/8/30 0030.
 */
public class HomePageActivity extends AppCompatActivity {

    private MaterialListView mListView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = (MaterialListView) findViewById(R.id.material_listview);
        mContext = this;

        //添加卡片
        Acard();

        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                Log.d("CARD_TYPE", card.getTag().toString());
            }

            @Override
            public void onItemLongClick(Card card, int position) {
                Log.d("LONG_CLICK", card.getTag().toString());
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

    private void Acard(){
        Card card = new Card.Builder(this)
                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                .withProvider(BasicImageButtonsCardProvider.class)
                .setTitle("A卡片")
                .setDescription("I've been generated on runtime!")
                .setDrawable(R.mipmap.dog)
                .endConfig()
                .build();

        mListView.add(card);
    }
}
