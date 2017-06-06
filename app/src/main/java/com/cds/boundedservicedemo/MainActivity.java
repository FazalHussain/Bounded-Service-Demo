package com.cds.boundedservicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection myServicecon;

    private MyService.MyServiceBinder myServiceBinder;
    private MyService myservice;
    private boolean isBound;
    private TextView tv;
    private Button bindbtn;
    private Button unbindbtn;
    private Intent serviceintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //startService(new Intent(this,MyService.class));
    }

    private void init() {
        serviceintent = new Intent(this,MyService.class);
        startService(serviceintent);
        tv = (TextView) findViewById(R.id.tv);
        bindbtn = (Button) findViewById(R.id.bindbtn);
        unbindbtn = (Button) findViewById(R.id.unbindbtn);
    }

    public void BindClick(View view){
        try{
            if(myServicecon==null){
                myServicecon = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        isBound = true;
                        myServiceBinder = (MyService.MyServiceBinder)iBinder;
                        myservice = myServiceBinder.getService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        isBound = false;
                    }
                };

                bindService(serviceintent,myServicecon,BIND_AUTO_CREATE);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void unBindClick(View view){
        isBound = false;
        unbindService(myServicecon);
    }

    public void RandomClick(View view){
        if(isBound) {
            List<Integer> rand = myservice.getRandomNumber();
            for(int i: rand)
            tv.append(i + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceintent);
    }
}
