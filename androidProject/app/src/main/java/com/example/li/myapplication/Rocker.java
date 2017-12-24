package com.example.li.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by li on 2017/12/10.
 */

public class Rocker {
    //一般坐标的弧度；
    private  double degreesByNormalSystem = Double.NaN;
    //当前遥感的弧度；
    private double  currentRad = Double.NaN;
    //定义摇杆的颜色
    private int rockerColor = Color.GREEN;
    //定义摇杆的两个圆心坐标和半径 一百分比计算
    //摇杆右边界宽度相对于屏幕的百分比
    private static final float rockerCenterXMarginRight4ScreenWidthPercent = 0.05f;
    //摇杆右边界高度相对于屏幕的百分比
    private static final float rockerCenterYMarginRight4ScreenHeightPercent = 0.05f;
    //摇杆半径相对于屏幕的百分比
    private static final float rockerR4ScreenWidthPercent = 0.1f;
    private float SmallCenterX=120,SmallCenterY=120,SmallCenterR=20;
    private float   BigCenterX=120,BigCenterY=120,BigCenterR=40;
    private Paint paint;
    private boolean WORKING;

    public Rocker(int screenWidth,int screenHeight) {

        paint=new Paint();
        //设置画笔的透明度
        //paint.setAlpha(119);
        paint.setAlpha(0x77);
        BigCenterR=screenWidth*rockerR4ScreenWidthPercent;
        BigCenterX=SmallCenterX= screenWidth- BigCenterR*1.5f
                - rockerCenterXMarginRight4ScreenWidthPercent*screenWidth;
        BigCenterY=SmallCenterY=screenHeight-BigCenterR*1.5f
                -rockerCenterYMarginRight4ScreenHeightPercent*screenHeight;
        SmallCenterR=BigCenterR/2;
    }
    public void drawSelf(Canvas canvas){
        paint.setColor(rockerColor);
        paint.setAlpha(119);
        //画大圆
        canvas.drawCircle(BigCenterX,BigCenterY,BigCenterR,paint);
        //画小圆
        canvas.drawCircle(SmallCenterX,SmallCenterY,SmallCenterR,paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("原点在左上坐标系的弧度"+currentRad,20,20,paint);
        canvas.drawText("由该弧度计算得出角度:"+(currentRad*180)/Math.PI,20,40,paint);
        canvas.drawText("原点在左下坐标系角度:"+degreesByNormalSystem, 20, 60, paint);
    }

    public void begin(int touchX, int touchY) {

        if (isBigCircleInternal(touchX,touchY)) {
            WORKING=true;
         update(touchX,touchY);
        }else{
            WORKING=false;
        }
    }

    public void update(int touchX, int touchY) {
        currentRad=getRad(BigCenterX,BigCenterY,touchX,touchY);
        if (WORKING) {
            if (isBigCircleInternal(touchX,touchY)) {
                SmallCenterX = touchX;
                SmallCenterY = touchY;

            } else {
                setSmallCircleXY(BigCenterX, BigCenterY, BigCenterR, currentRad);
            }
        }
        degreesByNormalSystem=getDegrees(BigCenterX,BigCenterY,SmallCenterX,SmallCenterY);
    }

    private boolean isBigCircleInternal(int touchX,int touchY){
        if (Math.sqrt(Math.pow((BigCenterX - touchX), 2) +
                Math.pow((BigCenterY - touchY), 2)) <= (BigCenterR ))
            return  true;
        return false;
    }

    private double getDegrees(float bigCenterX, float bigCenterY, float smallCenterX, float smallCenterY) {
        float ret = (float)Math.atan((bigCenterY-smallCenterY)/(-smallCenterX))*180/(float)Math.PI;
        if(bigCenterX<smallCenterX){
            ret += 180;
        }else {
            ret += 360;
        }
        ret = ret >= 360 ? ret - 360: ret;
        return ret;
    }

    private void setSmallCircleXY(float bigCenterX, float bigCenterY, float bigCenterR, double currentRad) {
        SmallCenterX= (float) (bigCenterR*Math.cos(currentRad))+bigCenterX;
        SmallCenterY= (float) (bigCenterR*Math.sin(currentRad)+bigCenterY);

    }

    public void reset() {
        SmallCenterX=BigCenterX;
        SmallCenterY=BigCenterY;
        WORKING = false;
    }
    /*
    * 得到两点的弧度
    *
    *
    * */
    private double getRad(float bigCenterX, float bigCenterY, int touchX, int touchY) {
        //得到两点X的距离
        float dx=touchX-bigCenterX;
        //得到两点Y的距离
        float dy=touchY-bigCenterY;
        //算出斜边的距离
        float  l= (float) Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        //得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
        float cosAngle=dx/l;
        //通过反余弦定理得到角度的弧度
        float rad = (float) Math.acos(cosAngle);
        if (touchY<bigCenterY){
            rad=-rad;
        }
        return rad;
    }

    public boolean getWORKING() {
        return WORKING;
    }

    public double getCurrentRad() {
        return currentRad;
    }

    public double getDegreesByNormalSystem() {
        return degreesByNormalSystem;
    }
}
