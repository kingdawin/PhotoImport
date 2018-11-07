package com.example.bm.photoview.BitmapRegionDecoderDemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhy on 15/5/16.
 *
 * setInputStream里面去获得图片的真实的宽度和高度，以及初始化我们的mDecoder
 * onMeasure里面为我们的显示区域的rect赋值，大小为view的尺寸
 * onTouchEvent里面我们监听move的手势，在监听的回调里面去改变rect的参数，以及做边界检查，最后invalidate
 * 在onDraw里面就是根据rect拿到bitmap，然后draw了
 * ---------------------
 * 作者：鸿洋_
 * 来源：CSDN
 * 原文：https://blog.csdn.net/lmj623565791/article/details/49300989
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 */
public class LargeImageView extends View
{
    public static final String TAG="LargeImageView";
    private BitmapRegionDecoder mDecoder;
    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();
    //监听滑动手势
    private MoveGestureDetector mDetector;


    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    static
    {
        //设置色彩模式，默认值是ARGB_8888，在这个模式下，一个像素点占用4bytes空间，
        //一般对透明度不做要求的话，一般采用RGB_565模式，这个模式下一个像素点占用2bytes
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void initImageWidthHeight()
    {
        mImageWidth = mDecoder.getWidth();
        mImageHeight = mDecoder.getHeight();
    }

    public void setInputStream(InputStream is)
    {
        try
        {
//            mDecoder = BitmapRegionDecoder.newInstance(is, false);//此代码要在获取宽高之后执行，否则，获取的宽高为-1

            //-----------使用initImageWidthHeight代替-------------------------
           /* BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;*/
            //-----------------------------------------------------------------

            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            initImageWidthHeight();
            Log.i(TAG,"setInputStream mImageWidth="+mImageWidth+" mImageHeight="+mImageHeight);
            requestLayout();
            invalidate();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {

            try
            {
                if (is != null) is.close();
            } catch (Exception e)
            {
            }
        }
    }

    //创建滑动监听实例
    public void init()
    {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector()
        {
            @Override
            public boolean onMove(MoveGestureDetector detector)
            {
                Log.i(TAG,"MoveGestureDetector onMove mImageWidth="+mImageWidth+" getWidth="+getWidth()
                +" mImageHeight="+mImageHeight+" getHeight()="+ getHeight()
                );
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();

                if (mImageWidth > getWidth())
                {
                    Log.i(TAG,"mImageWidth > getWidth()");
                    mRect.offset(-moveX, 0);
                    checkWidth();
                    invalidate();
                }
                if (mImageHeight > getHeight())
                {
                    Log.i(TAG,"mImageHeight > getHeight()");
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();
                }

                return true;
            }
        });
    }


    private void checkWidth()
    {


        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth)
        {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }

        if (rect.left < 0)
        {
            rect.left = 0;
            rect.right = getWidth();
        }
    }


    private void checkHeight()
    {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight)
        {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0)
        {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }


    public LargeImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.i(TAG,"onTouchEvent");
        mDetector.onToucEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.i(TAG,mRect.left+","+mRect.top+","+mRect.right+","+mRect.bottom);
        Bitmap bm = mDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bm, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG,"onMeasure");
        //view的宽高
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //图片宽高
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;

    }


}
