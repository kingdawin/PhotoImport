package com.example.bm.photoview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bm.photoview.worldmapviewDemo.ImageViewerActivity;

/**
 * Created by liuheng on 2015/6/21.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //单个ImageView
    public void img(View view) {
        startActivity(new Intent(this, ImgActivity.class));
    }

    //普通ViewPager
    public void viewpager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }


    //单个Imageview点击放大
    public void imgclick(View view) {
        startActivity(new Intent(this, ImgClick.class));
    }

    //ImageView点击浏览大图
    public void photobrowse(View view) {
        startActivity(new Intent(this, PhotoBrowse.class));
    }

    public void imageview(View view) {
        startActivity(new Intent(this, ImageViewActivity.class));
    }

    public void loadLargeImageView(View view)
    {
       startActivity(new Intent(this,LargeImageViewActivity.class));
    }
    public void loadLargeSurfaceViewImage(View view)
    {
        startActivity(new Intent(this,ImageViewerActivity.class));
    }
    //区块方法，加载大图
    public void loadLargeImage(View view)
    {
        startActivity(new Intent(this,SingleDemoActivity.class));
    }
}
