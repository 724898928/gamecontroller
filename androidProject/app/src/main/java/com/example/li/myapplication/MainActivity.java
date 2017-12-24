package com.example.li.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.li.myapplication.Util.LogUtil;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    /*static {
        System.loadLibrary("native-lib");
    }*/
  private static final String  TAG = "lixin MainActivity class ...";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        //
        LogUtil.level=2;
        LogUtil.d(TAG,"OnCreate!");
        setContentView(new MySurfaceView(this));
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
   // public native String stringFromJNI();
}
