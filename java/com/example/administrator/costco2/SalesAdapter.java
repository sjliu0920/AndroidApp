package com.example.administrator.costco2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by double on 8/18/16.
 */
public class SalesAdapter extends ArrayAdapter<SaleItem> {
    private int resourceId;
    private Callback mCallback;

    public interface Callback {
        void click(View v, int position);
    }

    public SalesAdapter(Context context, int textViewResourceId,
                        List<SaleItem> objects, Callback callback) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mCallback = callback;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        SaleItem item = getItem(position); // 获取当前项的salesitem实例
        ImageView itemImage = (ImageView) view.findViewById(R.id.sales_image);
        TextView itemName = (TextView) view.findViewById(R.id.sales_name);
        TextView itemCharge = (TextView) view.findViewById(R.id.sales_charge);
        TextView itemJudge = (TextView) view.findViewById(R.id.sales_judge);
        itemName.setText(item.getName());
        itemCharge.setText(item.getCharge());
        itemJudge.setText(item.getJudge());
        itemImage.setImageResource(item.getImageId());

        final Button bt_check = (Button) view.findViewById(R.id.check_item);
        //bt_check.setOnClickListener(new View.OnClickListener());
        bt_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.click(v, position);
            }
        });

        return view;
    }

//    public void onClick(View v) {
//        mCallback.click(v, mPosition);
//    }
}
