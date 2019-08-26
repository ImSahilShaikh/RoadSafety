package com.example.deva.block;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApp extends Application {
    private double speed;
    private String msg;

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public double getSpeed()
    {
        return speed;
    }

    public void setMsg(String m)
    {
        this.msg = m;
        SharedPreferences sp = getSharedPreferences("msg", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("msg",msg);
        edit.apply();
    }

    public String getMsg()
    {
        SharedPreferences sp = getSharedPreferences("msg", MODE_PRIVATE);
        String result = sp.getString("msg", "Automatic Generated msg: Riding.....");
        return msg;
    }
}
