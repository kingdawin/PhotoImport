package com.example.bm.photoview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.bm.photoview.BitmapRegionDecoderDemo.LargeImageView;

import java.io.IOException;
import java.io.InputStream;


public class LargeImageViewActivity extends AppCompatActivity
{
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_view);
//        testBitmapOption();
        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        try
        {
            InputStream inputStream = getAssets().open("world.jpg");
            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}