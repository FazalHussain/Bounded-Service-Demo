package com.cds.boundedservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by fazal on 5/26/2017.
 */

public class MyService extends Service {

    private int randomnumber;
    private boolean isRandomnumgenerate;

    private ArrayList<Integer> listRandom = new ArrayList<>();

    private int MIN = 0;
    private int MAX = 100;

    private static final String TAG = "MyService";

    class MyServiceBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }

    private IBinder mBinder = new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"Service Binded");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"Service unbinded");
        return super.onUnbind(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"Service Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRandomnumgenerate = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        generateRandomNumber();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        return START_STICKY;
    }

    private void generateRandomNumber() throws InterruptedException {
        while (isRandomnumgenerate) {
            try {
                Thread.sleep(1000);
                randomnumber = MIN + (int) (Math.random() * ((MAX - MIN) + 1));
                listRandom.add(randomnumber);
                Log.d(TAG, String.valueOf(randomnumber));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void stopGenerateRandomNumber() {
        isRandomnumgenerate = false;
    }

    public ArrayList<Integer> getRandomNumber(){
        return listRandom;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGenerateRandomNumber();
        Log.d(TAG,"Service Destroy");
    }
}
