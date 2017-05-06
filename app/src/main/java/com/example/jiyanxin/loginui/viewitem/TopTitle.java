package com.example.jiyanxin.loginui.viewitem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.others.ActivityMain;

/**
 * Created by JIYANXIN on 2017/4/17.
 */

public class TopTitle extends LinearLayout implements View.OnClickListener {

    public final static String BROADCAST_ACTION_EXIT = "com.example.jiyanxin.loginui.EXIT";
    private Button back;
    public TextView title;
    private Button settings;
    public Activity activity_from;

    public TopTitle(Context context, AttributeSet attrs){
            super(context,attrs);
            LayoutInflater.from(context).inflate(R.layout.top_title,this);

            back = (Button)findViewById(R.id.back);
            title = (TextView)findViewById(R.id.title_text);
            settings = (Button)findViewById(R.id.settings);

            back.setOnClickListener(this);
            settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v ){
        switch (v.getId()){
            case R.id.back:
                Intent intent = new Intent();
                intent.setAction(BROADCAST_ACTION_EXIT);
                activity_from.sendBroadcast(intent);
                break;
            case R.id.settings:
                Toast.makeText(activity_from,"设置",Toast.LENGTH_SHORT).show();
//                Intent intent_settings = new Intent(activity_from,ActivityMain.class);
//                activity_from.startActivity(intent_settings);
                break;
            default:
                break;
        }
    }
}
