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
    
    
    public void syncUserDB(File db){
        final boolean isExist = db.exists();
        // 云端是否存在DB
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            }
        }).flatMap(new Function<String, Observable<?>>() {
            @Override
            public Observable<?> apply(String o) throws Exception {
                // 四种情况
                // 本地没有 云端有 下载
                download();
                // 本地有 云端没有  上传
                upload();
                // 本地有 云端有 版本版本检查接口 上传/下载/什么都不干
                checkUserDBVersion();
                // 本地 云端 均无 什么都不干
                return null;
            }
        }).map(new Function<Object, Object>() {
            @Override
            public Object apply(Object o) throws Exception {
                return null;
            }
        }).subscribe();
    }

    private boolean upload(){
        return false;
    }

    private boolean download(){
        return false;
    }

    private int checkUserDBVersion(){
        //0
        //1
        //2
        return  0;
    }


}
