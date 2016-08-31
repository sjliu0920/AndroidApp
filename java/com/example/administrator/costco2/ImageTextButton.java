/**
 * Created by double on 8/18/16.
 */
package com.example.administrator.costco2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageTextButton extends RelativeLayout {

    private ImageView imgView;
    private TextView  textView;
    private int       id;

    public ImageTextButton(Context context) {
        super(context,null);
    }

    public ImageTextButton(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.img_text_bt, this,true);

        this.imgView = (ImageView)findViewById(R.id.imgview);
        this.textView = (TextView)findViewById(R.id.textview);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public int getId() {
        return id;
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }
}
