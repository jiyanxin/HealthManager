package com.example.jiyanxin.loginui.myhome;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiyanxin.loginui.account.LoginActivity;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.viewitem.Bottom;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

/**
 * Created by JIYANXIN on 2017/4/10.
 */

public class MyHome extends Activity implements View.OnClickListener{

    private LinearLayout about;
    private LinearLayout password;
    private LinearLayout message;
    private Button lag_out;
    private TextView usernameText;
    private TextView phoneText;
    private TextView emailText;
    private TextView heightText;
    private TextView weighText;

    private TopTitle topTitle;
    private Bottom bottom;

    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_home);
        findView();
    }

    @Override
    protected void onStart(){
        super.onStart();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        if(pref.contains("mail") == false){
            editor.putString("mail","***");
        }
        if(pref.contains("phone") == false){
            editor.putString("phone","***");
        }
        if(pref.contains("height") == false){
            editor.putString("height","***");
        }
        if(pref.contains("weigh") == false){
            editor.putString("weigh","***");
        }
        editor.commit();
        usernameText.setText(pref.getString("account",""));
        phoneText.setText(pref.getString("phone",""));
        emailText.setText(pref.getString("mail",""));
        heightText.setText(pref.getString("height",""));
        weighText.setText(pref.getString("weigh",""));

    }

    public void findView(){
        /*********************首尾两部分********************/

        topTitle = (TopTitle)findViewById(R.id.top_myhome);
        topTitle.activity_from = MyHome.this;
        topTitle.title.setText("我的账户");

        bottom = (Bottom)findViewById(R.id.bottom_myhome);
        bottom.activity_from = MyHome.this;
        bottom.myHome.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

        /*********************中间部分**********************/
        usernameText = (TextView)findViewById(R.id.username_text);
        usernameText.setOnClickListener(this);
        phoneText = (TextView)findViewById(R.id.phone_text);
        phoneText.setOnClickListener(this);
        emailText = (TextView)findViewById(R.id.mail_text);
        emailText.setOnClickListener(this);
        heightText = (TextView)findViewById(R.id.height_text);
        heightText.setOnClickListener(this);
        weighText = (TextView)findViewById(R.id.weigh_text);
        weighText.setOnClickListener(this);
        password = (LinearLayout)findViewById(R.id.pass);
        password.setOnClickListener(this);
        message = (LinearLayout)findViewById(R.id.message);
        message.setOnClickListener(this);
        about = (LinearLayout)findViewById(R.id.about);
        about.setOnClickListener(this);

        about = (LinearLayout)findViewById(R.id.about);
        about.setOnClickListener(this);
        lag_out = (Button)findViewById(R.id.lag_out);
        lag_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            /****************中间部分*****************/
            case R.id.username_text:
                Intent intent_username = new Intent(MyHome.this,ReviseUsername.class);
                startActivity(intent_username);
                break;
            case R.id.phone_text:
                Intent intent_phone = new Intent(MyHome.this,RevisePhone.class);
                startActivity(intent_phone);
                break;
            case R.id.mail_text:
                Intent intent_mail = new Intent(MyHome.this,ReviseMail.class);
                startActivity(intent_mail);
                break;
            case R.id.pass:
                Intent intent_password = new Intent(MyHome.this,RevisePassword.class);
                startActivity(intent_password);
                break;
            case R.id.height_text:
                Intent intent_height = new Intent(MyHome.this,ReviseHeight.class);
                startActivity(intent_height);
                break;
            case R.id.weigh_text:
                Intent intent_weigh = new Intent(MyHome.this,ReviseWeigh.class);
                startActivity(intent_weigh);
                break;
            case R.id.message:
                Toast.makeText(this, "我的消息", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lag_out:
                Intent intent_lagout = new Intent(this,LoginActivity.class);
                SharedPreferences userSettings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.putBoolean("islogin",false);
                editor.commit();
                startActivity(intent_lagout);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(TopTitle.BROADCAST_ACTION_EXIT)){//发来关闭action的广播
                finish();
            }
        }
    };
}
