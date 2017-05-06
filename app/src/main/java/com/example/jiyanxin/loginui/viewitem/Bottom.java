package com.example.jiyanxin.loginui.viewitem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.homepage.HomePage;
import com.example.jiyanxin.loginui.myhome.MyHome;
import com.example.jiyanxin.loginui.sports.Sports;
import com.example.jiyanxin.loginui.states.Heartbeats;

/**
 * Created by JIYANXIN on 2017/4/17.
 */

public class Bottom extends LinearLayout implements View.OnClickListener {

    public Button homePage;
    public Button states;
    public Button sports;
    public Button myHome;
    public Activity activity_from;

    public Bottom(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.buttom_layout,this);

        homePage = (Button)findViewById(R.id.home_page);
        states = (Button)findViewById(R.id.states);
        sports = (Button)findViewById(R.id.sports);
        myHome = (Button)findViewById(R.id.my_home);

        homePage.setOnClickListener(this);
        states.setOnClickListener(this);
        sports.setOnClickListener(this);
        myHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.home_page:
                Intent intent_homepage = new Intent(activity_from,HomePage.class);
                activity_from.startActivity(intent_homepage);
                break;
            case R.id.states:
                Intent intent_states = new Intent(activity_from,Heartbeats.class);
                activity_from.startActivity(intent_states);
                break;
            case R.id.sports:
                Intent intent_sports = new Intent(activity_from,Sports.class);
                activity_from.startActivity(intent_sports);
                break;
            case R.id.my_home:
                Intent intent_myhome = new Intent(activity_from,MyHome.class);
                activity_from.startActivity(intent_myhome);
                break;
            default:
                break;
        }
    }
}
