package com.example.jiyanxin.loginui;

import java.text.Format;
import java.util.ArrayList;

/**
 * Created by JIYANXIN on 2017/4/22.
 */

public class Statistics {

    private int sumInt=0;
    private int maxInt;
    private int minInt;
    private int averageInt;

    private float sumFloat=0;
    private float maxFloat;
    private float minFloat;
    private float averageFloat;

    private double sumDouble=0;
    private double maxDouble;
    private double minDouble;
    private double averageDouble;

    public Statistics(){

    }

    /**
     * Int
     * @param list
     * @return
     */
    public int SumInt( ArrayList<Integer> list){
        int i=0;
        for( i = 0; i < list.size(); i++){
            sumInt = sumInt + list.get(i);
        }
        return sumInt;
    }

    public ArrayList<Integer> SortInt(ArrayList<Integer> list){
        int i,j;
        for( i = 1; i < list.size(); i++){
            for(j = 0; j < list.size() - i;j++ ){
                if( list.get(j) < list.get(j+1)){
                    int temp;
                    temp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,temp);
                }
            }
        }
        maxInt = list.get(list.size()-1);
        minInt = list.get(0);
        return list;
    }

    public int getMaxInt(){
        return maxInt;
    }

    public int getMinInt(){
        return minInt;
    }

    public int AverageInt(ArrayList<Integer> list){
        averageInt = sumInt/list.size();
        return averageInt;
    }

    /**
     * Float
     * @param list
     * @return
     */
    public float SumFlaot(ArrayList<Float> list){
        int i=0;
        for( i = 0; i < list.size(); i++){
            sumFloat = sumFloat + list.get(i);
        }
        return sumFloat;
    }

    public ArrayList<Float> SortFloat(ArrayList<Float> list){
        int i,j;
        for( i = 1; i < list.size(); i++){
            for(j = 0; j < list.size() - i;j++ ){
                if( list.get(j) < list.get(j+1)){
                    float temp;
                    temp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,temp);
                }
            }
        }
        maxFloat = list.get(list.size()-1);
        minFloat = list.get(0);
        return list;
    }

    public float getMaxFloat(){
        return maxFloat;
    }

    public float getMinFloat(){
        return minFloat;
    }

    public float AverageFlaot(ArrayList<Float> list){
        averageFloat = sumFloat/list.size();
        return averageFloat;
    }

    /**
     * Double
     * @param list
     * @return
     */

    public double SumDouble(ArrayList<Double> list){
        int i=0;
        for( i = 0; i < list.size(); i++){
            sumDouble = sumDouble + list.get(i);
        }
        return sumDouble;
    }

//    public ArrayList<Double> SortDouble(ArrayList<Double> list){
//        int i,j;
//        for( i = 1; i < list.size(); i++){
//            for(j = 0; j < list.size() - i;j++ ){
//                if( list.get(j) < list.get(j+1)){
//                    Double temp;
//                    temp = list.get(j);
//                    list.set(j,list.get(j+1));
//                    list.set(j+1,temp);
//                }
//            }
//        }
//        minDouble = list.get(list.size()-1);
//        maxDouble = list.get(0);
//        return list;
//    }


    public double getMaxDouble(ArrayList<Double> list){
        int i;
        maxDouble=list.get(0);
        for(i=1;i<list.size();i++){
            if(list.get(i)>maxDouble){
                maxDouble=list.get(i);
            }
        }
        return maxDouble;
    }

    public double getMinDouble(ArrayList<Double> list){
        int i;
        minDouble=list.get(0);
        for(i=1;i<list.size();i++){
            if(list.get(i)<minDouble){
                minDouble=list.get(i);
            }
        }
        return minDouble;
    }

    public double AverageDouble(ArrayList<Double> list){
        averageDouble = this.SumDouble(list)/list.size();
        return averageDouble;
    }

}
