package com.example.administrator.costco2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    private WebView webView;
    int indexInList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        indexInList = intent.getIntExtra("extra_data", 0);

        pic_hdl = new PicHandler();
        if(indexInList == 0) {
            setContentView(R.layout.origin_pic);
            imageView = (ScaleImageView) findViewById(R.id.origin_pic);
            imageView.setImageResource(R.mipmap.costco);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
        }
        else {
            setContentView(R.layout.origin_pic_web);
            webView = (WebView) findViewById(R.id.origin_pic_web);
            webView.getSettings().setJavaScriptEnabled(true);

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int scale = dm.densityDpi;
            /*
            if (scale == 240)  { //
                webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            } else if (scale == 160) {
                webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            } else {
                webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
            }
            */

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String
                        url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            // webView.loadUrl("http://www.baidu.com");
            webView.loadUrl("http://192.168.1.106:8080/Web1/login.html");

            return;
        }

        // LoadPicThread thread = new LoadPicThread();
        // thread.start();
    }

    class LoadPicThread extends Thread {
        public void run() {
            if(indexInList == 0) {
                Bitmap img = getUrlImage("http://192.168.1.106/baby.png");
                Message msg = pic_hdl.obtainMessage();
                msg.obj = img;
                pic_hdl.sendMessage(msg);
            }
            else {
                /*
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url); // 根据传入的参数再去加载新的网页
                        return true; // 表示当前WebView可以处理打开新网页的请求,不用借助
                    }
                });
                */
                // webView.loadUrl("http://192.168.1.106:8080/Web1/login.html");
                webView.loadUrl("http://www.baidu.com");
            }
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
