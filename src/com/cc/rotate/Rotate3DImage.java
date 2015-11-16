package com.cc.rotate;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class Rotate3DImage extends View
{
    private Paint bitmapPaint;
    
    private Resources resources;
    
    private Bitmap bitmap1;
    
    private Bitmap bitmap2;
    
    private int width, height, bitmapWidth1, bitmapHeight1, bitmapWidth2, bitmapHeight2;
    
    private Rect viewRect = new Rect();
    
    private Rect bitmapRect = new Rect();
    
    /**是否允许触摸动画*/
    private boolean touchEnable = true;
    
    private int mapM, touchMX = 0, touchMY = 0;
    
    /**分别为X Y上相对于点击点的位置，点击点最开始旋转，然后根据相对位置逐个旋转，math.max(mapMX,mapMY)以X Y上最大的相对位置来进行翻转*/
    private int mapMX, mapMY;
    
    /**每一个分割图片的边长*/
    private int oneBitWidth1, oneBitHeight1, oneBitWidth2, oneBitHeight2, oneImgWidth,
            oneImgHeight;
            
    private BitmapRegionDecoder mDecoder;
    
    private BitmapRegionDecoder mDecoder2;
    
    /**true为显示第二幅图片，false为显示第一副图片，超过90度显示第二张*/
    private boolean nexts[];
    
    private boolean isStart = false;
    
    /**false为正序，true为反序*/
    private boolean isAntitone = false;
    
    private int size = 5;
    
    private Camera mCamera = new Camera();;
    
    private float degrees[];
    
    private Bitmap[] bitmaps1;
    
    private Bitmap[] bitmaps2;
    
    private float d;
    
    /** 1为正旋转。-1为反旋转*/
    private int isPositiveRotate = 1;
    
    /**设定以／为false，以＼为true*/
    private boolean isPositiveDiagonal = true;
    
    private static final BitmapFactory.Options options = new BitmapFactory.Options();
    
    static
    {
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }
    
    /**
     * @return the {@link #isAntitone}
     */
    public boolean isAntitone()
    {
        return isAntitone;
    }

    /**
     * @param isAntitone the {@link #isAntitone} to set
     */
    public void setAntitone(boolean isAntitone)
    {
        this.isAntitone = isAntitone;
    }

    /**
     * @return the {@link #isPositiveRotate}
     */
    public boolean getIsPositiveRotate()
    {
        return isPositiveRotate > 0 ? false : true;
    }

    /**
     * @param isPositiveRotate the {@link #isPositiveRotate} to set
     */
    public void setIsPositiveRotate(boolean isPositiveRotate)
    {
        this.isPositiveRotate = isPositiveRotate ? -1 : 1;
    }

    public Rotate3DImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        
        resources = context.getResources();
    }
    
    @SuppressLint("NewApi")
    public void initBitmap(int bitmapRes1,int bitmapRes2)
    {
        bitmap1 = BitmapFactory.decodeResource(resources, bitmapRes1);
        bitmap2 = BitmapFactory.decodeResource(resources, bitmapRes2);
        
        bitmapRect.left = 0;
        bitmapRect.top = 0;
        bitmapRect.right = bitmap1.getWidth();
        bitmapRect.bottom = bitmap1.getHeight();
        
        try
        {
            InputStream is = resources.openRawResource(R.drawable.img1);
            InputStream is2 = resources.openRawResource(R.drawable.img2);
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            mDecoder2 = BitmapRegionDecoder.newInstance(is2, false);
            
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            
            BitmapFactory.decodeStream(is, null, tmpOptions);
            bitmapWidth1 = tmpOptions.outWidth;
            bitmapHeight1 = tmpOptions.outHeight;
            
            BitmapFactory.decodeStream(is2, null, tmpOptions);
            bitmapWidth2 = tmpOptions.outWidth;
            bitmapHeight2 = tmpOptions.outHeight;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        
        if (isStart)
        {
            
            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    Matrix matrix1 = new Matrix();
                    
                    mCamera.save();
                    
                    //mx为横向位置，j→
                    mapMX = j - touchMX;
                    if (mapMX < 0)
                    {
                        mapMX = -mapMX;
                    }
                    //mY为纵向位置，i↓
                    mapMY = i - touchMY;
                    
                    if (mapMY < 0)
                    {
                        mapMY = -mapMY;
                    }
                    
                    mapM = Math.max(mapMY, mapMX);
                    
                    canvas.save();
                    canvas.rotate(45,oneImgWidth * j + oneImgWidth / 2,
                                oneImgHeight * i + oneImgHeight / 2);
                            
                    if(isPositiveDiagonal)
                        mCamera.rotateY(isPositiveRotate*degrees[mapM]);
                    else 
                        mCamera.rotateX(isPositiveRotate*degrees[mapM]);
                    
                    mCamera.getMatrix(matrix1);
                    mCamera.restore();
                    
                    int centerX1 = oneImgWidth / 2;
                    int centerY1 = oneImgHeight / 2;
                    
                    Matrix matrix2 = new Matrix();
                    matrix2.postRotate(-45);
                    matrix1.setConcat(matrix1, matrix2);
                    
                    matrix1.postTranslate(oneImgWidth * j, oneImgHeight * i);
                    matrix1.preTranslate(-centerX1, -centerY1);
                    matrix1.postTranslate(centerX1, centerY1);
                    
                    if (nexts[mapM])
                    {
                        canvas.drawBitmap(bitmaps2[i * size + j],
                                matrix1,
                                bitmapPaint);
                    }
                    else
                    {
                        canvas.drawBitmap(bitmaps1[i * size + j],
                                matrix1,
                                bitmapPaint);
                    }
                    
                    canvas.restore();
                    
                }
                
            }
        }
        else
        {
            if(bitmap1 != null)
                canvas.drawBitmap(bitmap1, bitmapRect, viewRect, bitmapPaint);
        }
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (touchEnable)
        {
            float x = event.getX();
            float y = event.getY();
            
            if (!isStart)
                startAni(x, y,1500);
        }
        
        return super.onTouchEvent(event);
    }
    
    public boolean isTouchEnable()
    {
        return touchEnable;
    }
    
    public void setTouchEnable(boolean touchEnable)
    {
        this.touchEnable = touchEnable;
        
    }
    
    /**
     * @return the {@link #size}
     */
    public int getSize()
    {
        return size;
    }

    /**
     * @param size the {@link #size} to set
     */
    public void setSize(int size)
    {
        this.size = size;
    }

    public void startAni(int duration)
    {
        startAni(0,0,duration);
    }
    
    /**
     * @return the {@link #isPositiveDiagonal}
     */
    public boolean isPositiveDiagonal()
    {
        return isPositiveDiagonal;
    }

    /**
     * @param isPositiveDiagonal the {@link #isPositiveDiagonal} to set
     */
    public void setPositiveDiagonal(boolean isPositiveDiagonal)
    {
        this.isPositiveDiagonal = isPositiveDiagonal;
    }

    @SuppressLint("NewApi")
    public void startAni(float x, float y,int duration)
    {
        if(bitmap1 == null && bitmap2 == null)
            return;
        
        oneBitWidth1 = bitmapWidth1 / size;
        oneBitHeight1 = bitmapHeight1 / size;
        
        oneBitWidth2 = bitmapWidth2 / size;
        oneBitHeight2 = bitmapHeight2 / size;
        
        oneImgWidth = width / size;
        oneImgHeight = height / size;
        
        if(!isPositiveDiagonal)
        {
            if(x != 0 | y != 0 ) {
                touchMX = (int) (x / oneImgWidth);
                touchMY = (int) (y / oneImgHeight);
                
            }else if(isAntitone)
            {
                touchMX = size - 1;
                touchMY = 0;
            }else
            {
                touchMX = 0;
                touchMY = size - 1;
            }
            
        } else
        {
            if(x != 0 | y != 0 ) {
                touchMX = (int) (x / oneImgWidth);
                touchMY = (int) (y / oneImgHeight);
                
            }else if(isAntitone)
            {
                touchMX = size - 1;
                touchMY = size - 1;
            }else 
            {
                touchMX = 0;
                touchMY = 0;
            }
        }
            
        bitmaps1 = new Bitmap[size * size];
        bitmaps2 = new Bitmap[size * size];
        degrees = new float[size];
        
        nexts = new boolean[size];
        
        isStart = true;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                Bitmap bitmap = Bitmap.createScaledBitmap(mDecoder.decodeRegion(
                        new Rect(j * oneBitWidth1,i * oneBitHeight1, oneBitWidth1 * (j + 1),oneBitHeight1 * (i + 1)), options),
                        oneImgWidth,oneImgHeight,false);
                        
                bitmaps1[i * size + j] = bitmap;
                
                Bitmap bitmap2 = Bitmap.createScaledBitmap( mDecoder2.decodeRegion(
                        new Rect(j * oneBitWidth2,i * oneBitHeight2, oneBitWidth2 * (j + 1),oneBitHeight2 * (i + 1)), options),
                        oneImgWidth, oneImgHeight, false);
                bitmaps2[i * size + j] = bitmap2;
            }
            nexts[i] = false;
            
        }
        
        d = (size + 1) / 2.0f;
        
        My3dAnimation anim1 = new My3dAnimation();
        anim1.setDuration(duration);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setAnimationListener(new AnimationListener()
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
        
        startAnimation(anim1);
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        
        viewRect.left = 0;
        viewRect.top = 0;
        viewRect.right = width;
        viewRect.bottom = height;
        
    }
    
    public class My3dAnimation extends Animation
    {
        
        private final float mFromDegrees = 0f;
        
        private final float mToDegrees = 180f;
        
        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t)
        {
            
            for (int i = size - 1; i >= 0; i--)
            {
                if (interpolatedTime < i / (size + 1.0f))
                {
                    degrees[i] = 0;
                }
                else
                {
                    /** 在这里把时间分成了（size + 1）分，每一份90度，也就是当一个旋转到90度时，下一个就开始旋转
                         d * (mToDegrees - mFromDegrees) = (size + 1) / 2.0f * 180 = 90 * (size + 1)  这样就刚好把时间分配完全*/
                    degrees[i] = mFromDegrees + (d * (mToDegrees - mFromDegrees) * (interpolatedTime - i / (size + 1.0f)));
                    if (degrees[i] >= 90f)
                    {
                        nexts[i] = true;
                        degrees[i] += 180;//第二张要加180度，不然旋转过去的图片是倒的
                    }
                    if (degrees[i] > 360)
                    {
                        degrees[i] = 0;//图片先旋转完后设置为0，让其他未旋转完的继续旋转
                    }
                }
            }
            
            invalidate();
        }
    }
    
}
