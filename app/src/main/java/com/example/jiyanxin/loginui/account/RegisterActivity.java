package com.example.jiyanxin.loginui.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiyanxin.loginui.BaseActivity;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.others.MyAccount;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JIYANXIN on 2017/4/5.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText username;
    private EditText password_f;
    private EditText password_s;
    private EditText mail;
    private EditText height;
    private EditText weigh;
    private Button register;
    private TextView login;
    private MyAccount myAccount;
    private int result=0;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register);
        findView();
    }

    public void findView(){
        /**
         *根据id找到组件
         **/
        username = (EditText)findViewById(R.id.register_username_edit);
        password_f = (EditText)findViewById(R.id.register_password_edit_first);
        password_s = (EditText)findViewById(R.id.register_password_edit_second);
        mail = (EditText)findViewById(R.id.register_mail_edit);
        height = (EditText)findViewById(R.id.height_edit);
        weigh = (EditText)findViewById(R.id.weigh_edit);
        register = (Button)findViewById(R.id.register);
        login = (TextView)findViewById(R.id.login_link);
        /**
         * 注册监听器
         */
        //注册用户注册监听事件
        register.setOnClickListener(this);
        //处理登录切换监听事件
        login.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

    }
    /**
     * 处理监听事件
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
                Register();
                break;
            case R.id.login_link:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("loginFlag",false);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    /**
     * 处理注册事件
     */
    public void Register(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = RegisterNet();
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        switch (result){
            case 1:
                Toast.makeText(RegisterActivity.this,"注册成功！请登录！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case -1:
                Toast.makeText(RegisterActivity.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                break;
            case -2:
                Toast.makeText(RegisterActivity.this,"注册失败，请重新注册！",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            }
    }
    /**
     * 处理注册联网事件
     */
    public int  RegisterNet()throws IOException {
        int returnResult=0;
        String infor="";
        String user_name = username.getText().toString();
        String pass_word_f = password_f.getText().toString();
        String pass_word_s =password_s.getText().toString();
        String email = mail.getText().toString();
        String h = height.getText().toString();
        String w = weigh.getText().toString();

        if( null == user_name || user_name.length()<=0){
            Looper.prepare();
            Toast.makeText(RegisterActivity.this,"请输入账号！",Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        else if( null == pass_word_f || pass_word_f.length()<=0){
            Looper.prepare();
            Toast.makeText(RegisterActivity.this,"请输入密码！",Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        else if( null == pass_word_s || pass_word_s.length()<=0){
            Looper.prepare();
            Toast.makeText(RegisterActivity.this,"请再次输入密码！",Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        else if( !pass_word_f.equals(pass_word_s) ){
            Looper.prepare();
            Toast.makeText(RegisterActivity.this,"两次输入的密码不一致！",Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }

        String params="uid="+user_name+'&'+"pwd="+pass_word_f+'&'+"height="+h+'&'+"weigh="+w+'&'+"mail="+email;
        String urlstr = "http://120.25.207.101/register.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(3000);
        http.setDoOutput(true);
        http.setRequestMethod("POST");

        OutputStream out = http.getOutputStream();
        out.write(params.getBytes());
        /* JSONObject obj = new JSONObject();
            try {
                obj.put("uid", user_name);
                obj.put("pwd", pass_word);
                out.write(obj.toString().getBytes());
             }catch(Exception e){
                e.printStackTrace();
            }*/
        out.flush();
        out.close();
        //读取网页返回数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line="";
        StringBuilder stringBuilder =new StringBuilder();
        while(null != (line=bufferedReader.readLine())){
            stringBuilder.append(line);//写缓冲区
        }
        String result = stringBuilder.toString();//返回数据结果
        try{
            JSONObject jsonObject = new JSONObject(result);
            returnResult = jsonObject.getInt("states");//获取JSON数据中的states字段值
            infor = jsonObject.getString("info");
        }catch (Exception e){
            Log.e("log_tag","the Error parsing data"+e.toString());
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
