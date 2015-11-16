package com.cc.rotate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class MyImage2 extends View
{
    
    private Paint bitmapPaint;
    
    private Resources resources;
    
    private Bitmap bitmap1;
    
    private int width, height;
    
    public MyImage2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        
        resources = context.getResources();
        initBitmap();
    }
    
    private void initBitmap()
    {
        bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.img5);
        
    }
    
    private Camera mCamera = new Camera();
    
    float angle = 0;
    
    int centerX1,centerY1;
    
    Matrix matrix2;
    Matrix matrix1 = new Matrix();
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        
        if(isStart)
        {
            canvas.rotate(45,centerX1,centerY1);
            
            mCamera.save();
            mCamera.rotateX(angle);
            mCamera.getMatrix(matrix1);
            mCamera.restore();
            
            matrix1.setConcat(matrix1, matrix2);
            matrix1.preTranslate(-centerX1, -centerY1);
            matrix1.postTranslate(centerX1, centerY1);
            
            canvas.drawBitmap(bitmap1, matrix1, bitmapPaint);
        }else
        {
            canvas.drawBitmap(bitmap1, 0, 0, bitmapPaint);
        }
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        
    }
    
    boolean isStart = false;
    
    public void start(){
        if(!isStart)
        {
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(1500);
            animation.setStartOffset(100); 
            animation.setAnimationListener(new AnimationListener()
            {
                
                public void onAnimationStart(Animation animation)
                {
                }
                
                public void onAnimationRepeat(Animation animation)
                {
                }
                
                public void onAnimationEnd(Animation animation)
                {
                    isStart = false;
                }
            });
            

            centerX1 = width / 2;
            centerY1 = height / 2;
            
            bitmap1 = Bitmap.createScaledBitmap(bitmap1, width, height, false);
            
            matrix2 = new Matrix();
            matrix2.postRotate(-45);
            
            startAnimation(animation); 
            isStart = true;
        }
        
    }
    
    Animation animation = new Animation(){
        
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            
            angle = interpolatedTime*200;
            if(angle > 180)
            {
                angle = 180;
            }
            invalidate(); 
        };
    };
    
}
