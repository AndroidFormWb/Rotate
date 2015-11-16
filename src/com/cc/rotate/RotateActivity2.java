package com.cc.rotate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RotateActivity2 extends Activity
{

    MyImage2 img;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        
        img = (MyImage2) findViewById(R.id.myimg);
        
        findViewById(R.id.btn).setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
                img.start();
            }
        });
    }
}
