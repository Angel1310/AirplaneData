package com.example.airplanedata;

public class PointClass {
    long xValue;
    int yValue;
    public PointClass(){

    }
    public PointClass(long xValue, int yValue){
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public long getxValue() {
        return xValue;
    }

    public void setxValue(long xValue) {
        this.xValue = xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }


}
