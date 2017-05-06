package com.example.jiyanxin.loginui.viewitem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jiyanxin.loginui.R;

import org.w3c.dom.Text;

/**
 * Created by JIYANXIN on 2017/4/14.
 */

public class Analyse extends RelativeLayout {

    public TextView maxValue;
    public TextView minValue;
    public TextView argValue;
    public String max;
    public String min;
    public String arg;

    public Analyse(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.analyse,this);

        maxValue = (TextView)findViewById(R.id.Max_h_v);
        minValue = (TextView)findViewById(R.id.Min_h_v);
        argValue = (TextView)findViewById(R.id.Avg_h_v);

        maxValue.setText("90");
        minValue.setText("65");
        argValue.setText("75");
    }

    public void setMaxValue(String max){
       // this.max = max;
        maxValue.setText(max);
    }

    public void setMinValue(String min){
        //this.min = min;
        minValue.setText(min);
    }

    public void setArgValue(String arg){
       // this.arg = arg;
        argValue.setText(arg);
    }

}
