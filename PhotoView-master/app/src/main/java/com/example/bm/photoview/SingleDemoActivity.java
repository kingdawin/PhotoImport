package com.example.bm.photoview;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bm.photoview.factory.InputStreamBitmapDecoderFactory;
import com.example.bm.photoview.largeImageDemo.LargeImageView;
import com.example.bm.photoview.largeImageDemo.MapScaleView;

import java.io.IOException;
import java.io.InputStream;



public class SingleDemoActivity extends FragmentActivity {
    private LargeImageView largeImageView;
    private ToggleButton toggleButton;
    private ToggleButton mapScaleButton;
    MapScaleView mapScaleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singledemo);
        mapScaleView=(MapScaleView)findViewById(R.id.mapScaleView);
        mapScaleView.calculateMeterPerPix(new PointF(10,10),new PointF(10,20));
        largeImageView =(LargeImageView)findViewById(R.id.imageView);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mapScaleButton=(ToggleButton)findViewById(R.id.mapScaleViewButton);
        toggleButton.setOnCheckedChangeListener(onCheckedChangeListener);
        mapScaleButton.setOnCheckedChangeListener(onCheckedChangeListener);
        largeImageView.setOnClickListener(onClickListener);
        largeImageView.setOnLongClickListener(onLongClickListener);
        largeImageView.setOnDoubleClickListener(onDoubleClickListener);
        largeImageView.setOnScaleListener(new LargeImageView.OnScaleListener() {
            @Override
            public void onScale(float scale) {
                Log.i("xxxx","onScale");
                //在这里进行比例尺宽和现实值计算
                mapScaleView.updateMapScaleView(scale);
                mapScaleView.invalidate();
            }
        });

        try {
//            String fileName = getIntent().getStringExtra("file_name");
            InputStream inputStream = getAssets().open("aaa.jpg");
            largeImageView.setImage(new InputStreamBitmapDecoderFactory(inputStream), null);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    largeImageView.setScale(0.5f);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == largeImageView) {
                Toast.makeText(getApplicationContext(), "点击事件", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            if (v == largeImageView) {
                Toast.makeText(getApplicationContext(), "长按事件", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId())
            {
                case R.id.toggleButton:
                    largeImageView.setEnabled(!isChecked);

                    break;
                case R.id.mapScaleViewButton:
                    largeImageView.isSettingMapScale=isChecked;
                    if(!isChecked)
                    {
                        if(largeImageView.mapScalePointSetting.size()==2)
                        {
                            mapScaleView.init(largeImageView.mapScalePointSetting.get(0),
                                    largeImageView.mapScalePointSetting.get(1),20,largeImageView.setScale
                                    );
                            mapScaleView.invalidate();
                        }
                    }
                    break;
            }
        }
    };

    private LargeImageView.CriticalScaleValueHook criticalScaleValueHook = new LargeImageView.CriticalScaleValueHook() {
        @Override
        public float getMinScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMinScale) {
            return 1;
        }

        @Override
        public float getMaxScale(LargeImageView largeImageView, int imageWidth, int imageHeight, float suggestMaxScale) {
            return 4;
        }
    };

    private LargeImageView.OnDoubleClickListener onDoubleClickListener = new LargeImageView.OnDoubleClickListener() {
        @Override
        public boolean onDoubleClick(LargeImageView view, MotionEvent event) {
            float fitScale = view.getFitScale();
            float maxScale = view.getMaxScale();
            float minScale = view.getMinScale();
            String message = "双击事件 minScale:" + minScale + " maxScale:" + maxScale + " fitScale:" + fitScale;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            //返回true 拦截双击缩放的事件
            return false;
        }
    };
}
