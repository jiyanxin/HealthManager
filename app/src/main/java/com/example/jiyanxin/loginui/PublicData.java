package com.example.jiyanxin.loginui;

import java.util.ArrayList;

/**
 * Created by JIYANXIN on 2017/4/23.
 */

public class PublicData {
    PublicData(){}

    public static int step=0;
    public static int stepOld=20000;
    public static Boolean stepFlag = false;
/****************健康综合********************/
    public static ArrayList<Double> yListHealthSum = new ArrayList<>();
    public static ArrayList<String> xRawDatasHealthSum = new ArrayList<>();
    public static int NxHealthSum = 5;
    public static int NxyHealthSum = 4;
    public static int NyHealthSum = (NxHealthSum - 1) * NxyHealthSum + 1;
    public static int NbaseHealthSum = 1;
    public static int tempHealthSum=0;
    public static double maxHealthSum;
    public static double minHealthSum;
    public static double argHealthSum;

    /****************运动综合********************/
    public static ArrayList<Double> yListSports = new ArrayList<>();
    public static ArrayList<String> xRawDatasSports = new ArrayList<>();
    public static int NxSports = 5;
    public static int NxySports = 4;
    public static int NySports = (NxSports - 1) * NxySports + 1;
    public static int NbaseSports = 1;
    public static int tempSports=0;
    public static double maxSports;
    public static double minSports;
    public static double argSports;

    /****************心率综合********************/
    public static ArrayList<Double> yListHeartbeats = new ArrayList<>();
    public static ArrayList<String> xRawDatasHeartbeats = new ArrayList<>();
    public static int NxHeartbeats = 5;
    public static int NxyHeartbeats = 4;
    public static int NyHeartbeats= (NxHeartbeats - 1) * NxyHeartbeats + 1;
    public static int NbaseHeartbeats = 1;
    public static int tempHeartbeats=0;
    public static double maxHeartbeats;
    public static double minHeartbeats;
    public static double argHeartbeats;

    /****************体温综合********************/
    public static ArrayList<Double> yListTemperature = new ArrayList<>();
    public static ArrayList<String> xRawDatasTemperature = new ArrayList<>();
    public static int NxTemperature = 5;
    public static int NxyTemperature = 4;
    public static int NyTemperature = (NxTemperature - 1) * NxyTemperature + 1;
    public static int NbaseTemperature = 1;
    public static int tempTemperature=0;
    public static double maxTemperature;
    public static double minTemperature;
    public static double argTemperature;

    /****************血氧饱和度综合********************/
    public static ArrayList<Double> yListOxygenBlood = new ArrayList<>();
    public static ArrayList<String> xRawDatasOxygenBlood = new ArrayList<>();
    public static int NxOxygenBlood = 5;
    public static int NxyOxygenBlood= 4;
    public static int NyOxygenBlood = (NxOxygenBlood - 1) * NxyOxygenBlood + 1;
    public static int NbaseOxygenBlood = 1;
    public static int tempOxygenBlood=0;
    public static double maxOxygenBlood;
    public static double minOxygenBlood;
    public static double argOxygenBlood;

    public static String Number2String(ArrayList<Double> list){
        int i;
        String array="";

        for(i = 0; i<10;i++){
            array = array+list.get(i)+"#";
        }
        return array;
    }

    public static ArrayList<Double> String2Number(String array){
        int i;
        ArrayList<Double> list = new ArrayList<>();
        String regularEx = "#";
        String[] str=null;
        int N =0;

        str = array.split(regularEx);

        N =str.length;
        for(i=0;i<N;i++){
            double temp= Double.parseDouble(str[i]);
            list.add(temp);
        }
        return list;
    }

}
