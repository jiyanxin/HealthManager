package com.example.jiyanxin.loginui.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.jiyanxin.loginui.R;


/**
 * Created by JIYANXIN on 2017/4/21.
 */

public class TopStates extends LinearLayout implements View.OnClickListener{
    public Button heartbeats;
    public Button temperature;
    public Button oxygenBlood;
    public Activity activity_from;

    public TopStates(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.top_states,this);

        heartbeats = (Button)findViewById(R.id.button_heartbeat);
        temperature = (Button) findViewById(R.id.button_temperature);
        oxygenBlood = (Button)findViewById(R.id.button_oxygenblood);

        heartbeats.setOnClickListener(this);
        temperature.setOnClickListener(this);
        oxygenBlood.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_heartbeat:
                Intent intent_heartbeats = new Intent(activity_from,Heartbeats.class);
                activity_from.startActivity(intent_heartbeats);
                break;
            case R.id.button_temperature:
                Intent intent_temperature = new Intent(activity_from,Temperature.class);
                activity_from.startActivity(intent_temperature);
                break;
            case R.id.button_oxygenblood:
                Intent intent_oxygenblood = new Intent(activity_from,OxygenBlood.class);
                activity_from.startActivity(intent_oxygenblood);
                break;
            default:
                break;
        }
    }

}
