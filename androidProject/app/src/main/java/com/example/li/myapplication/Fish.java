package com.example.li.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.li.myapplication.Util.LogUtil;

import java.util.Random;

/**
 * Created by li on 2017/12/10.
 */

public class Fish {
    private float x;
    private float y;
    private float width;
    private  float height;
    private  int speed=10;

    private static  final String TAG="lixin Fish class...";
    private int currentFrame=0;
    private Bitmap bitmap;


    public Fish (float touchX, float touchY) {
        this.x=touchX;
        this.y=touchY;
        degrees = new Random().nextInt(90);
        LogUtil.d(TAG,"fish Fish()  touchX="+touchX+" touchY="+touchY);
   }
   private  void updateFrame(){
       currentFrame++;
       if (currentFrame>=10){
           currentFrame=0;
       }
   }
    private int degrees;
    //sorry ,this fish was Catched;
    //return true  if this fish was Catched or false;
    boolean beCatched(float touchX,float touchY ){
        Rect rect =new Rect((int)x,(int)y,(int)(x+width),(int)(y+height));
        LogUtil.d(TAG,"fish beCatched()  return value= "+rect.contains((int)touchX,(int)touchY));
        return rect.contains((int)touchX,(int)touchY);

    }
    /*
    public void draw(MySurfaceView mySurfaceView, Canvas canvas, Bitmap[] fishBitmap, Matrix matrix, float touchY){
      //  canvas.drawBitmap(fishBitmap,matrix,touchY,new Paint());

    }*/
    public void draw(MySurfaceView mySurfaceView,Canvas canvas,Bitmap[] fishBitmap, Matrix matrix,double waterY){
        updateFrame();
        if (mySurfaceView.getRocker().getWORKING()&&!
                Double.isNaN( mySurfaceView.getRocker().getDegreesByNormalSystem())){
            degrees = (int) mySurfaceView.getRocker().getDegreesByNormalSystem();
          double  Rad = mySurfaceView.getRocker().getCurrentRad();
            x= (float) (x+speed*Math.cos(Rad));
            y= (float) (y+speed*Math.sin(Rad));
        }
        generateBmpWithDegree(fishBitmap,matrix,degrees);
        checkBorder(mySurfaceView, (float) waterY);
        width=bitmap.getWidth();
        height=bitmap.getHeight();
        //canvas.drawBitmap(fishBitmap[0],x,y,new Paint());
        canvas.drawBitmap(bitmap,x,y,new Paint());
        LogUtil.d(TAG,"fish draw()  width="+width+" height="+height);
        canvas.drawText("鱼的坐标："+"x:"+x+"y"+y, 20, 80, new Paint());
    }

    private void generateBmpWithDegree(Bitmap[] fishBitmap, Matrix matrix, double degrees) {
        if (degrees>=90&&degrees<=270){
            //将矩阵设置成单位矩阵
            matrix.reset();
            //设置缩放比，
            matrix.postScale(1,-1);
            bitmap=Bitmap.createBitmap(fishBitmap[currentFrame],0,0,
                    fishBitmap[currentFrame].getWidth(),fishBitmap[currentFrame].getHeight(),matrix,true);
        }else{
            bitmap=fishBitmap[currentFrame];
        }
        matrix.reset();
        //旋转图片
        matrix.postRotate((float)degrees,bitmap.getWidth()/2,bitmap.getHeight()/2);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    private void checkBorder(View view, float waterY) {
        if(x<=0){
            x = 0;
        }
        if(x>=view.getWidth()-width){
            x=view.getWidth()-width;
        }
        if(y<waterY){
            y = waterY;
        }
        if(y>=view.getHeight()-height){
            y=view.getHeight()-height;
        }
    }


}
