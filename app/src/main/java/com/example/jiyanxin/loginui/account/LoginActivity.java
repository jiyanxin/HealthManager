package com.example.jiyanxin.loginui.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiyanxin.loginui.BaseActivity;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.Service.LongRunningService;
import com.example.jiyanxin.loginui.homepage.HealthSumService;
import com.example.jiyanxin.loginui.homepage.HomePage;
import com.example.jiyanxin.loginui.others.MyAccount;
import com.example.jiyanxin.loginui.sports.SportsService;
import com.example.jiyanxin.loginui.states.HeartbeatsService;
import com.example.jiyanxin.loginui.states.OxygenBloodService;
import com.example.jiyanxin.loginui.states.TemperatureService;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**************基本变量**************/
    private EditText username;
    private EditText password;
    private Button login;
    private TextView register;
    private String user_name;
    private String pass_word;
    private String phone;
    private String mail;
    private String height;
    private String weigh;
    /**************记住密码*************/
    private CheckBox rememberPassword;
    private CheckBox autoLogin;
    private boolean isRemember;
    private boolean isLogin;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    /**************辅助变量***************/
    private int result = 0;
    private static final int SHOW_RESPONSE = 0;

    private WebView webView;

    /**
     * 活动创建函数
     *
     * @param saveInstanceState
     */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver, filter);

        /**
         *根据id找组件
         **/
        username = (EditText) findViewById(R.id.username_edit);
        password = (EditText) findViewById(R.id.password_edit);
        login = (Button) findViewById(R.id.signin_button);
        register = (TextView) findViewById(R.id.register_link);
        rememberPassword = (CheckBox) findViewById(R.id.remember_pwd);
        autoLogin = (CheckBox) findViewById(R.id.auto_login);
        /**
         *注册登记监听器
         **/
        //注册登录事件
        login.setOnClickListener(this);
        //注册用户注册切换事件
        register.setOnClickListener(this);
        //获取状态
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isRemember = pref.getBoolean("rememberPassword", false);
        isLogin = pref.getBoolean("islogin", false);

        if (isRemember) {
            String account = pref.getString("account", "");
            String pwd = pref.getString("password", "");
            username.setText(account);
            password.setText(pwd);
            rememberPassword.setChecked(true);
            if (isLogin) {
                autoLogin.setChecked(true);
                Intent intent = new Intent(LoginActivity.this,HomePage.class);
                startActivity(intent);
            }
        }

        StartService();

//        webView = (WebView)findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String
//                    url) {
//                view.loadUrl(url); // 根据传入的参数再去加载新的网页
//                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
//            }
//        });
//        webView.loadUrl("http://120.25.207.101/login.php");
    }

    public void StartService(){
        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);

        Intent intentHealthSum = new Intent(this, HealthSumService.class);
        startService(intentHealthSum);

        Intent intentSports = new Intent(this, SportsService.class);
        startService(intentSports);

        Intent intentHeartbeats = new Intent(this, HeartbeatsService.class);
        startService(intentHeartbeats);

        Intent intentOxygenBlood = new Intent(this, OxygenBloodService.class);
        startService(intentOxygenBlood);

        Intent intentTemperature = new Intent(this, TemperatureService.class);
        startService(intentTemperature);
    }

    /**
     * 处理监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                login();
//                UpdateTextTask task = new UpdateTextTask(this);
//                task.execute(rememberPassword.isChecked(), autoLogin.isChecked());
                break;
            case R.id.register_link:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    /**
     * 登录事件处理函数
     */

    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = LoginNet();
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
         if (1 == result) {
             boolean flagLogin=true;
            editor = pref.edit();
            if (rememberPassword.isChecked()) {
                if(autoLogin.isChecked()){
                    editor.putBoolean("islogin", true);
                }
                editor.putBoolean("rememberPassword", true);
                editor.putString("account", user_name);
                editor.putString("password", pass_word);
                editor.putString("phone",phone);
                editor.putString("mail",mail);
                editor.putString("height",height);
                editor.putString("weigh",weigh);
            } else {
                if(autoLogin.isChecked()){
                    Toast.makeText(LoginActivity.this, "设置错误！", Toast.LENGTH_SHORT).show();
                    flagLogin = false;
                }
                editor.clear();
            }
            editor.commit();
             if(true == flagLogin) {
                 Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(LoginActivity.this, HomePage.class);
                 startActivity(intent);
             }
        }
            if (-2 == result) {
            Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
        }
            if (-1 == result) {
            Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 用户登录提交post请求
     * 向服务器提交数据用户名，密码
     * 返回JSON数据
     **/
    private int LoginNet() throws IOException {
        int returnResult = 0;
        String infor = "";
        /**获取用户名和密码**/
        user_name = username.getText().toString();
        pass_word = password.getText().toString();
        if (null == username || user_name.length() <= 0) {
            Looper.prepare();
            Toast.makeText(LoginActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return 0;
        }
        if (null == pass_word || pass_word.length() <= 0) {
            Looper.prepare();
            Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return 0;
        }
        String params = "uid=" + user_name + '&' + "pwd=" + pass_word;
        String urlstr = "http://120.25.207.101/login.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(3000);
        http.setDoOutput(true);
        http.setRequestMethod("POST");

        OutputStream out = http.getOutputStream();
        out.write(params.getBytes());
        out.flush();
        out.close();
        //读取网页返回数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while (null != (line = bufferedReader.readLine())) {
            stringBuilder.append(line);//写缓冲区
        }
        String result = stringBuilder.toString();//返回数据结果
        Message message = new Message();
        message.what = SHOW_RESPONSE;
        try {
            JSONObject jsonObject = new JSONObject(result);
            returnResult = jsonObject.getInt("states");//获取JSON数据中的states字段值
            phone =jsonObject.getString("phone");
            mail = jsonObject.getString("mail");
            height = jsonObject.getString("height");
            weigh = jsonObject.getString("weigh");
            infor = jsonObject.getString("info");
        } catch (Exception e) {
            Log.e("log_tag", "the Error parsing data" + e.toString());
        }
        return returnResult;
    }

    private TextView tv;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TopTitle.BROADCAST_ACTION_EXIT)) {//发来关闭action的广播
                finish();
            }
        }
    };
}
    /****
     * 遗忘的孩子AsyncTask
     */
//    class UpdateTextTask extends AsyncTask<Boolean,Integer,Integer> {
//        private Context context;
//
//        UpdateTextTask(Context context) {
//            this.context = context;
//        }
//
//        /**
//         * 运行在UI线程中，在调用doInBackground()之前执行
//         */
//        @Override
//        protected void onPreExecute() {
//            Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
//        }
//        /**
//         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
//         */
//        @Override
//        protected Integer doInBackground(Boolean... params) {
//            boolean isRemember;
//            boolean isLogin;
//
//            isRemember = params[0];
//            isLogin = params[1];
//            try {
//                result = LoginNet();
//            } catch (IOException e) {
//                Looper.prepare();
//                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//            if (1 == result) {
//                editor = pref.edit();
//                if (isRemember ) {
//                    if(isLogin){
//                        editor.putBoolean("islogin", true);
//                    }
//                    editor.putBoolean("rememberPassword", true);
//                    editor.putString("account", user_name);
//                    editor.putString("password", pass_word);
//                } else {
//                    if(isLogin){
//                        // Looper.prepare();
//                        Toast.makeText(LoginActivity.this, "设置错误！", Toast.LENGTH_SHORT).show();
//                        //  Looper.loop();
//                    }
//                    editor.clear();
//                }
//                editor.commit();
//                Looper.prepare();
//                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//                Intent intent = new Intent(LoginActivity.this, HomePage.class);
//                startActivity(intent);
//            }
//            if (-2 == result) {
//                Looper.prepare();
//                Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//            if (-1 == result) {
//                Looper.prepare();
//                Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//            return null;
//        }
//
//        /**
//         * 运行在ui线程中，在doInBackground()执行完毕后执行
//         */
//        @Override
//        protected void onPostExecute(Integer integer) {
//            Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
//        }
//
//        /**
//         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
//         */
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            tv.setText(""+values[0]);
//        }
//    }

