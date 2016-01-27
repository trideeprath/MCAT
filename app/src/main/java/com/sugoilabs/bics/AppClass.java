package com.sugoilabs.bics;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.splunk.mint.Mint;

/**
 * Created by Trideep on 26-01-2016.
 */
public class AppClass extends Application {

    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("TestApplication", "Uncaught exception is: ", ex);

             // log it & phone hom
            androidDefaultUEH.uncaughtException(thread, ex);

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        //androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        //Thread.setDefaultUncaughtExceptionHandler(handler);
        Mint.initAndStartSession(getApplicationContext(), "84b3f5b4");
    }




}
