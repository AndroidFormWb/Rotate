package com.cc.rotate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn1).setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,RotateActivity2.class));
            }
        });
        
        findViewById(R.id.btn2).setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,RotateActivity1.class));
            }
        });
    }

}
