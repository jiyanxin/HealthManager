package com.example.jiyanxin.loginui.sports;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.TextView;

import com.example.jiyanxin.loginui.PublicData;

import com.example.jiyanxin.loginui.viewitem.Analyse;
import com.example.jiyanxin.loginui.R;
import com.example.jiyanxin.loginui.viewitem.LineGraphicView;
import com.example.jiyanxin.loginui.viewitem.Bottom;
import com.example.jiyanxin.loginui.viewitem.TopTitle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JIYANXIN on 2017/4/10.
 */

public class Sports extends Activity implements View.OnClickListener,Handler.Callback{

    private Calendar taskTime;
    private Calendar currentTime;
    private TextView realTime;
    private CircleSports circleSports;

    private LineGraphicView healthSports;

    private ArrayList<Double> yList;
    private ArrayList<String> xRawDatas;

    private Analyse sportsAnalyse;

    private TopTitle topTitle;
    private Bottom bottom;
    private int sportsValue = 0;

    SharedPreferences pref;

    private TextView text;
    private Handler delayHandler;
    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));

    private static final String TAG="nsc";
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;//返回服务
    public static final int REQUEST_SERVER = 2;//取消服务
    private long TIME_INTERVAL = 500;

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            try{
                messenger = new Messenger(service);
                Message msg = Message.obtain(null,MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;//replyTo消息管理器
                Log.d(TAG,"msg ="+ msg);
                messenger.send(msg);//发送消息出去
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    update();
                    break;
            }
            super.handleMessage(msg);
        }
        void update() {
            pref.getInt("time",0);
            SharedPreferences.Editor editor = pref.edit();
            if(!pref.contains("time")){
                editor.putInt("time",0);
            }
            if(!pref.contains("sports")){
                editor.putInt("sports",0);
            }

            editor.commit();

            //sportsValue = pref.getInt("sports",0);
            taskTime = Calendar.getInstance();
            taskTime.set(taskTime.HOUR,10);
            taskTime.set(taskTime.AM_PM,1);
            taskTime.set(taskTime.MINUTE, 25);

            currentTime = Calendar.getInstance();
            currentTime.getTime();

            if(taskTime.get(taskTime.HOUR) == currentTime.get(currentTime.HOUR) &&
                    taskTime.get(taskTime.MINUTE) == currentTime.get(currentTime.MINUTE) &&
                    taskTime.get(taskTime.AM_PM) == currentTime.get(currentTime.AM_PM) &&
                    PublicData.stepFlag == false){
                PublicData.stepOld = StepDcretor.CURRENT_SETP;
                PublicData.stepFlag = true;
                realTime.setText("fuck");
                /*********这里写每日的步行处理程序********/
            }else{
                PublicData.stepFlag = false;
            }

            //sportsValue=StepDcretor.CURRENT_SETP - PublicData.stepOld;
            sportsValue=20000;

            healthSports.setData(PublicData.yListSports,PublicData.xRawDatasSports,0, 16, 2);
            healthSports.invalidate();

            sportsAnalyse.setMaxValue(""+PublicData.maxSports*1000);
            sportsAnalyse.setMinValue(""+PublicData.minSports*1000);
            sportsAnalyse.setArgValue(""+PublicData.argSports*1000);
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sports);
        findView();
        delayHandler = new Handler(this);
        setupService();//启动服务
    }

    @Override
    protected void onStart(){
        super.onStart();
        timer.schedule(task, 0, 1000 * 1); //启动timer

    }

    public void findView(){

        realTime = (TextView)findViewById(R.id.sports_title_realtime);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        topTitle = (TopTitle)findViewById(R.id.top_sports);
        topTitle.activity_from = Sports.this;
        topTitle.title.setText("运动");

        bottom = (Bottom)findViewById(R.id.bottom_sports);
        bottom.activity_from = Sports.this;
        bottom.sports.setBackgroundColor(Color.parseColor("#FF02CAE1"));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TopTitle.BROADCAST_ACTION_EXIT);
        registerReceiver(mBroadcastReceiver,filter);

        healthSports = (LineGraphicView) findViewById(R.id.lineViewSports);
        sportsAnalyse = (Analyse)findViewById(R.id.sports_analyse);

        initLineGraphicView();

        circleSports = (CircleSports)findViewById(R.id.circle_sports);
        circleSports.setMax(10000);
        circleSports.setProgressSync(5000);
        circleSports.text = 2000;
    }

    @Override
    public void onClick(View v){

    }

    public void initLineGraphicView(){
        /*****************初始化设置*******************/
        healthSports.setMstyle(LineGraphicView.Linestyle.Curve);
        healthSports.setmPaintColor(getResources().getColor(R.color.greenyellow));
        healthSports.setmPaintColorX(getResources().getColor(R.color.color_f2f2f2));
        healthSports.setmPaintColorY(getResources().getColor(R.color.color_999999));
        healthSports.setmPaintWidth(5f,8f,3f);
        healthSports.setCircleSize(LineGraphicView.CircleSize.Mid);
        /*******************输入y坐标*************************/

        if(PublicData.yListSports.size()< PublicData.NySports){
            PublicData.yListSports.clear();
            for(int  i=0;i< PublicData.NySports;i++){
                PublicData.yListSports.add(3.458d);
            }
        }

        if(PublicData.xRawDatasSports.size()< PublicData.NxSports){
            PublicData.xRawDatasSports.clear();
            for(int  i=0;i< PublicData.NxSports;i++){
                PublicData.xRawDatasSports.add("10:10");
            }
        }
        healthSports.setData(PublicData.yListSports, PublicData.xRawDatasSports,0, 16, 2);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(TopTitle.BROADCAST_ACTION_EXIT)){//发来关闭action的广播
                finish();
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    /**
     * 启动服务
     */
    private void setupService() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,StepService.class);
        //使用这个ServiceConnection，客户端可以绑定到一个service，通过把它传给bindService()
        //第一个bindService()的参数是一个明确指定了要绑定的service的Intent．
        //第二个参数是ServiceConnection对象．
        //第三个参数是一个标志，它表明绑定中的操作．它一般应是BIND_AUTO_CREATE，这样就会在service不存在时创建一个．其它可选的值是BIND_DEBUG_UNBIND和BIND_NOT_FOREGROUND,不想指定时设为0即可．。
        bindService(intent, conn, BIND_AUTO_CREATE);//BIND_AUTO_CREATE =1
        startService(intent);
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch(msg.what){
            case MSG_FROM_SERVER:
                Log.d(TAG,"text="+ msg.getData().getInt("step"));
                //text.setText(msg.getData().getInt("step")+"");//显示记步数
                circleSports.text = msg.getData().getInt("step");
                //延时500ms发送值为REQUEST_SERVER 消息
                delayHandler.sendEmptyMessageDelayed(REQUEST_SERVER, TIME_INTERVAL);
                break;
            case REQUEST_SERVER:
                try{
                    Message message = Message.obtain(null,MSG_FROM_CLIENT);//发送消息
                    message.replyTo = mGetReplyMessenger;
                    Log.d(TAG,"message="+ message);
                    messenger.send(message);
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {// 停止timer
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unbindService(conn);//解除服务的绑定
    }
}
