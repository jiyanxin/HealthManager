package com.example.jiyanxin.loginui;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JIYANXIN on 2017/4/19.
 */

public class HttpConnection {

    private String url;


    public HttpConnection(){}

    public HttpConnection(String _url){
        this.url = _url;
    }

    public String HttpConnectionSendAndReveice(String params,String urlstr) {
        String result="";
        //建立网络连接
        try {
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
            result = stringBuilder.toString();//返回数据结果
        }catch(Exception e){
            Log.e("log_tag", "the Error parsing data" + e.toString());
        }
        return  result;
    }


    public void HttpConnectionSend(){

    }

    public void HttpConnectionReveice(){

    }
}
