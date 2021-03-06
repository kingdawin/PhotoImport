package com.example.bm.photoview.largeImageDemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MapScaleView extends View {
    //比例尺级数
    public static final int Meters[]=new int[]{1,2,5,10,20,50,100,200,500,1000};
    //比例尺最大最小宽
    public static final double MAX_Width=200;
    public static final double MIN_Width=50;
    public static final String TAG="MapScaleView";
    //m/pix 比例尺宽每个像素代表多少m
    private double meterPerPix;
    //缩放比1的M/pix
    private double meterPerPixBase;
//    private float distanceMeter;
    //设置值所在级下标
    public int levelIndex;
    double currentWidthPix;
    //用户设置的显示值
    int setMeter;
    int showMeter;
    int desiredWidth;
    int desireHeight;
    public MapScaleView(Context context) {
        super(context);
    }
    public MapScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MapScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(9);
        paint.setAntiAlias(true);

        textPaint.setTextSize(35);
        textPaint.setColor(Color.RED);
        textPaint.setStyle(Paint.Style.STROKE);

        float density = getResources().getDisplayMetrics().density;
        desiredWidth = (int) (100 * density);
        desireHeight= (int) (textPaint.getTextSize() * 3 + textPaint.getStrokeWidth());

    }

    /**
     *
     * @param startPoint
     * @param endPoint
     * @param setMeter
     * @param setScale
     */
    public void init(PointF startPoint, PointF endPoint,int setMeter, float setScale)
    {

        Log.i(TAG,"init setScale="+setScale);
        levelIndex=searchLevelIndex(setMeter);
        //初始值，TODO：假设用户输入值在要求的值，如果随意设定还要求最近值
        showMeter=setMeter;
        calculateMeterPerPix(startPoint,endPoint);
        meterPerPixBase=meterPerPix*setScale;

        updateMapScaleView(setScale);
    }
    public void calculateMeterPerPix(PointF startPoint,PointF endPoint)
    {
        //两点间像素点数
        double distancePix=Math.sqrt(Math.pow(startPoint.x-endPoint.x,2)+Math.pow(startPoint.y-endPoint.y,2));
        meterPerPix= showMeter/distancePix;
        Log.i(TAG,"distancePix="+distancePix);

    }

    //计算设置值所在级下标
    public int searchLevelIndex(int setMeter)
    {
        //对排序级数进行二分法查找
        int start = 0;
        int end = Meters.length - 1;
        while (start <= end) {
            int middle = (start + end) / 2;
            if (setMeter < Meters[middle]) {
                end = middle - 1;
            } else if (setMeter > Meters[middle]) {
                start = middle + 1;
            } else {
                return middle;
            }
        }
        //没有找到
        return -1;
    }
    //直接继承View的自定义控件需要重写此方法，并设置wrap_content时的自身大小，否则布局中使用wrap_conent相当于使用了match_parent
    //因为wrap_content对于specMode是AT_MOST
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=measureDimension(desiredWidth,widthMeasureSpec);
        int height=measureDimension(desireHeight,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        //精确模式使用布局设的dp作为宽高
        if (mode == View.MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == View.MeasureSpec.AT_MOST) {//wrap_content需要给定大小，否则和match_parent一样
            return Math.min(desiredSize, size);
        } else {
            return desiredSize;
        }
    }
    public void updateMapScaleView(float currentScale)
    {
        if(showMeter==0)
        {
            return;
        }
        int desireMeter=showMeter;
        Log.i(TAG,"meterPerPixBase="+meterPerPixBase+"  currentScale="+currentScale);
        double currentMeterPerPix=meterPerPixBase/currentScale;
        currentWidthPix=desireMeter/currentMeterPerPix;
        //放大地图->比例系数增加->当前比例尺宽增加->显示级别值变小
        while(currentWidthPix>MAX_Width)
        {
            if(levelIndex-1>=0)
            {
            //超过最大宽，降级
            desireMeter=Meters[--levelIndex];
            currentWidthPix=desireMeter/currentMeterPerPix;
            }else
                {
                    //没有找到设定的pix,用 超过最大设定像素值 显示
                    levelIndex=0;
                    desireMeter=Meters[levelIndex];
                    currentWidthPix=desireMeter/currentMeterPerPix;
                    break;
                }
        }

        //缩小地图
        while(currentWidthPix<MIN_Width)
        {
            if(levelIndex+1<Meters.length)
            {
                desireMeter=Meters[++levelIndex];
                currentWidthPix=desireMeter/currentMeterPerPix;
            }else{
                //没有找到设定的pix
                levelIndex=Meters.length-1;
                desireMeter=Meters[levelIndex];
                currentWidthPix=desireMeter/currentMeterPerPix;
                break;
            }
        }
            showMeter=desireMeter;
            Log.i(TAG,"currentWidthPix="+currentWidthPix+",showMeter="+showMeter+" currentScale="+currentScale+" showMeter="+showMeter);

    }
    //比例尺路径
    Path path=new Path();
    //开始的绘制坐标
    float startLeftPointX=10;
    float startLeftPointY=10;
    //左竖线结束点
    float rightEndPointX=10;
    float rightEndPointY=20;
    Paint paint=new Paint();
    Paint textPaint=new Paint();
    private final int outLineHeight=20;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.rewind();
        startLeftPointY=getHeight()/2-outLineHeight;
        //右竖线起点
        path.moveTo(startLeftPointX,startLeftPointY);
        //右竖线终点
        path.lineTo(rightEndPointX,startLeftPointY+outLineHeight);
        //横线结束点
        float horizonEndX=(float)(rightEndPointX+currentWidthPix);
        path.lineTo(horizonEndX,startLeftPointY+outLineHeight);
        //右竖线
        path.lineTo(horizonEndX,startLeftPointY);
        canvas.drawText(showMeter+"m",getWidth()/2,getHeight()/2,textPaint);
        canvas.drawPath(path,paint);
    }
}
