package com.example.administrator.costco2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/7/30.
 */
public class AllSaleList extends Activity {
    JSONObject obRet;
    private Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what > 0) {
                    try {
                        initAllSaleList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AllSaleList.this, "ret = " + msg.what, Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(new Runnable() {
            public void run() {
                getAllSaleList();

                Message message = new Message();
                try {
                    message.what = obRet.getInt("num");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(message); // 将Message对象发送出去
            }
        }).start();


    }

    private void getAllSaleList() {
        try {
            // { "method":"getAllSaleList" , "info":""}}
            JSONObject ob = new JSONObject();
            ob.put("method", "getAllTypeList");
            ob.put("info", "");

            Toast.makeText(this, ob.toString(), Toast.LENGTH_SHORT).show();

            obRet = getAllSaleListFromServer(ob);
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject getAllSaleListFromServer(JSONObject ob)
    {
        try {
            Socket socket = new Socket("192.168.1.106", 8009);

            // sent data to server
            OutputStream ou = socket.getOutputStream();
            ou.write(ob.toString().getBytes());
            ou.flush();

            InputStream input = socket.getInputStream();
            byte[]  bs = new byte[1024];
            int i = -1;
            while((i = input.read(bs)) != -1) {
                String str = new String(bs, "UTF-8");
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                return new JSONObject(str);
            }
        }
        catch (SocketTimeoutException aa ) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initAllSaleList() throws JSONException {
        int retNum = obRet.getInt("num");
        List<TypeItem> typeList = new ArrayList<>();
        JSONArray array = obRet.getJSONArray("array");
        for(int i = 0; i < retNum; i++) {
            JSONObject ob = array.getJSONObject(i);
            String type_name = ob.getString("type_name");
            TypeItem item1 = new TypeItem(type_name);
            typeList.add(item1);
        }

        TypeAdapter adapter = new TypeAdapter(AllSaleList.this,
                R.layout.type_item, typeList);
        ListView listView = (ListView) findViewById(R.id.type_list_view);
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

