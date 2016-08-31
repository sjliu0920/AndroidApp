package com.example.administrator.costco2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/7/30.
 */
public class AllSaleList extends Activity {
    private String[] data = { "衣服", "化妆品", "营养品"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AllSaleList.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.type_view);
        listView.setAdapter(adapter);

        final ImageTextButton bt2 = (ImageTextButton)findViewById(R.id.bt2);
        bt2.setText("男装");
        bt2.setTextColor(Color.rgb(0, 0, 0));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3)
            {
               if(index == 0) {
                   bt2.setText("男装");
                   bt2.setImgResource(R.mipmap.man_cloth);
               }
               else if (index == 1) {
                   bt2.setText("香水");
                   bt2.setImgResource(R.mipmap.beautify);
               }
               else if (index == 2) {
                   bt2.setText("保健");
                   bt2.setImgResource(R.mipmap.food);
               }
                bt2.setId(index);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Costco.this, "You clicked Button 1",
                //       Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AllSaleList.this, ChildSaleList.class);
                intent.putExtra("extra_data", bt2.getId());
                startActivity(intent);
            }
        });
    }
}
