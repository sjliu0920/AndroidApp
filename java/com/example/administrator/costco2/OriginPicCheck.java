package com.example.administrator.costco2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by double on 8/19/16.
 */
public class OriginPicCheck extends Activity{
    private Handler pic_hdl;
    ScaleImageView imageView;
    int indexInList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.origin_pic);
        Intent intent = getIntent();
        indexInList = intent.getIntExtra("extra_data", 0);

        imageView = (ScaleImageView) findViewById(R.id.origin_pic);
        pic_hdl = new PicHandler();
        if(indexInList == 0) {
            // get image from local disk
            imageView.setImageResource(R.mipmap.costco);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
        }
        else {
            LoadPicThread thread = new LoadPicThread();
            thread.start();
        }
    }

    class LoadPicThread extends Thread {
        public void run() {
            // Bitmap img = getUrlImage("http://www.nowamagic.net/librarys/images/random/rand_11.jpg");
            Bitmap img = getUrlImage("http://192.168.1.106/baby.png");
            Message msg = pic_hdl.obtainMessage();
            msg.obj = img;
            pic_hdl.sendMessage(msg);
        }
    }

    class PicHandler extends Handler {
        public void handleMessage(Message msg) {
            Bitmap myimg = (Bitmap)msg.obj;
            imageView.setImageBitmap(myimg);
        }
    }

    public Bitmap getUrlImage(String url) {
        Bitmap img = null;
        try {
            URL picurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            //conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            img = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}
