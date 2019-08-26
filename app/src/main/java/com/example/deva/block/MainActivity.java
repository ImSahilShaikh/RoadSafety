package com.example.deva.block;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent myIntent=new Intent(this,MyService.class);
        startService(myIntent);
    }

    public void setMessage(View v)
    {
        EditText ed = findViewById(R.id.ed1);

        String mes = ed.getText().toString();

        Toast.makeText(getApplicationContext(),"Message set as " + mes.toString(),Toast.LENGTH_SHORT).show();
        ((MyApp) getApplication()).setMsg(mes);
    }

    public void showSpeed(View view) {
        TextView tv1 = findViewById(R.id.tv1);
        String sp = Double.toString(((MyApp) getApplication()).getSpeed());
        Toast.makeText(getApplicationContext(), sp, Toast.LENGTH_SHORT).show();
        tv1.setText(sp);
    }

    public void next(View view) {
        Intent myIntent=new Intent(this,Loc.class);
        startActivity(myIntent);
    }
}