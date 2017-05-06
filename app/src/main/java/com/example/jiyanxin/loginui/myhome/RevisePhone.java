package com.example.jiyanxin.loginui.myhome;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiyanxin.loginui.BaseActivity;
import com.example.jiyanxin.loginui.HttpConnection;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by JIYANXIN on 2017/4/17.
 */

public class RevisePhone extends BaseActivity implements View.OnClickListener {
    private EditText revise;
    private Button sure;
    private int result;

    @Override
    protected void onCreate(Bundle saveInsatnceStstes){
        super.onCreate(saveInsatnceStstes);
        setContentView(R.layout.revise_phone);

        revise = (EditText) findViewById(R.id.revise_phone);
        sure = (Button)findViewById(R.id.revise_sure_phone);
        sure.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.revise_sure_phone){
            ReviseSure();
        }
    }

    private void ReviseSure(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = ReviseSureNet();
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(RevisePhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
        if (1 == result){
            Toast.makeText(RevisePhone.this,"修改电话号码成功！", Toast.LENGTH_SHORT).show();
            SharedPreferences userSettings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = userSettings.edit();
            editor.putString("phone",revise.getText().toString());
            editor.commit();
        }else if(-1 == result){
            Toast.makeText(RevisePhone.this, "该用户名不存在！", Toast.LENGTH_SHORT).show();
        } else if(-2 == result){
            Toast.makeText(RevisePhone.this, "更新失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private int  ReviseSureNet()throws IOException{

        int returnResult=0;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("account", "");

        String phone = revise.getText().toString();

        if( null == phone || phone.length()<=0){
            Looper.prepare();
            Toast.makeText(RevisePhone.this,"您未输入新数据！",Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }

        String params = "uid=" + username + '&' + "phone=" + phone;
        String urlstr = "http://120.25.207.101/revise_phone.php";

        HttpConnection httpConnection = new HttpConnection();
        String result = httpConnection.HttpConnectionSendAndReveice(params,urlstr);

        try {
            JSONObject jsonObject = new JSONObject(result);
            returnResult = jsonObject.getInt("states");//获取JSON数据中的states字段值
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data" + e.toString());
        }
        return returnResult;
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
