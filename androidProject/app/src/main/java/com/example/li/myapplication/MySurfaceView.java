package com.example.li.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.li.myapplication.Util.LogUtil;

/**
 * Created by li on 2017/12/10.
 */

class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder sfh;
    private static final String TAG="lixin MySurfaceView ...";
    private int SCREEN_W;
    private int SCREEN_H;
    private Thread mThread;
    private boolean TFlag;
    private  int TouchX;
    private int  TouchY;
    private  Fish fish;
    private   Rocker rocker;
    private Canvas canvas;
    private static Bitmap [] fishBitmap=new Bitmap[10];
    //创建矩阵
    Matrix matrix=new Matrix();
    float waterY=0;
    private  Paint paint;
    public MySurfaceView( Context context) {
        super(context);
        LogUtil.d(TAG,"MySurfaceView Create!");
        //实例holder ，控制事件
        sfh=this.getHolder();
        //为surfaceView添加状态监听
        sfh.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        setFocusable(true);
            }


    //SurfaceView 创建响应此函数
@Override
public void surfaceCreated(SurfaceHolder holder) {
        for (int i=0;i<fishBitmap.length;i++){
            fishBitmap[i]= BitmapFactory.decodeResource(this.getResources(),R.drawable.fish0+i);
        }
    SCREEN_W =this. getWidth();
    SCREEN_H = this.getHeight();
    TFlag=true;
    fish = new Fish(SCREEN_W/2,SCREEN_H/2);
    rocker=new Rocker(SCREEN_W,SCREEN_H);
    mThread =new Thread(this);
    mThread.start();

}


 public void myDrawRect(int left,int top){
        //锁定画布

   try {

       canvas  =sfh.lockCanvas();
       if (canvas!=null) {
           canvas.drawColor(Color.WHITE);
           rocker.drawSelf(canvas);
           fish.draw(this, canvas, fishBitmap, matrix, waterY);
      /* Paint pain=new Paint();
       pain.setColor(Color.RED);
       pain.setStyle(Paint.Style.FILL);
       canvas.drawRect(new Rect(left,top,left+80,top+80),pain);
       */
       }
   }catch (Exception e){
            e.printStackTrace();
   }finally {
       //释放画布
       if (canvas!=null)
       sfh.unlockCanvasAndPost(canvas);
   }


 }
/*
* 功能:封装游戏逻辑
* */
public void goGame(){
    ;
}
    @Override
    public void run() {

        while (TFlag){
            long startTime = System.currentTimeMillis();
            goGame();
            myDrawRect(TouchX,TouchY);

            long endTime = System.currentTimeMillis();
            /*1000ms/60=16.67ms 这里我们采用15，使得帧频限制在最大66.7帧
             *如果担心发热、耗电问题，同样可以适应稍大一些的的值。经测试80基本上为最大值
              *  */
           if(endTime-startTime<15){
                try {
                    Thread.sleep(15-(endTime-startTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
     //   TouchX=(int) event.getX();
       // TouchY=(int) event.getY();
        LogUtil.d(TAG,"onTouchEvent()");
         int MAX_TOUCHPOINTS=3;
        int pointerCount = event.getPointerCount();
        if (pointerCount>MAX_TOUCHPOINTS){
            pointerCount = MAX_TOUCHPOINTS;
        }
    //判断触摸事件是单触摸还是多触摸，

        int touchType = event.getActionMasked();
        switch (touchType){
            //第一个按下
            case MotionEvent.ACTION_DOWN:
                ;
                break;
                //非第一个按下
            case MotionEvent.ACTION_POINTER_DOWN:
                for(int i=0;i<pointerCount;i++){
                    int j=event.getPointerId(i);
                    int x1=(int) event.getX(j);
                    int y1= (int) event.getY(j);
                }
                ;
                break;
        }

        if(event.getAction()==MotionEvent.ACTION_UP){
            rocker.reset();
        }else{
            TouchX=(int) event.getX();
            TouchY=(int) event.getY();
            if (event.getAction()==MotionEvent.ACTION_DOWN){
                rocker.begin(TouchX,TouchY);
            }else if (event.getAction()==MotionEvent.ACTION_MOVE){
                rocker.update(TouchX,TouchY);
            }
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d(TAG,"surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        TFlag = false;
        LogUtil.d(TAG,"surfaceDestroyed() TFlag="+TFlag);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public Rocker getRocker() {
        return rocker;
    }
}
