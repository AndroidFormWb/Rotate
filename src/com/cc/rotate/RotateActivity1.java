package com.cc.rotate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RotateActivity1 extends Activity {

	private RadioGroup radioGroup1,radioGroup2 ,radioGroup3;
	
	private Rotate3DImage img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.order_ratate_activity);
		
		radioGroup1 = (RadioGroup) findViewById(R.id.rd1);
		radioGroup2 = (RadioGroup) findViewById(R.id.rd2);
		radioGroup3 = (RadioGroup) findViewById(R.id.rd3);
		
		img = (Rotate3DImage) findViewById(R.id.img);
		
		img.initBitmap(R.drawable.img1, R.drawable.img2); 
		
		radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                img.setAntitone(checkedId == R.id.antitone); 
            }
        });
		
		radioGroup2.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                img.setIsPositiveRotate(radioGroup2.getCheckedRadioButtonId() == R.id.positive_rotation);
            }
        });
		
		radioGroup3.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                img.setPositiveDiagonal(radioGroup3.getCheckedRadioButtonId() == R.id.positive_diagonal);
            }
        });
		
		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

			    img.startAni(1500);
			}
		});
	}
	
}
