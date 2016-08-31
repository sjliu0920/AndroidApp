package com.example.administrator.costco2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import com.example.administrator.costco2.SalesAdapter.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by double on 8/18/16.
 */
public class ChildSaleList extends Activity implements AdapterView.OnItemClickListener, Callback{
    private List<SaleItem> saleList = new ArrayList<SaleItem>();

    int FromId;
    JSONObject obRet;
    private Handler handler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_list);

        Intent intent = getIntent();
        FromId = intent.getIntExtra("extra_data", 0);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what > 0) {
                    try {
                        initSaleItem();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ChildSaleList.this, "ret = " + msg.what, Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(new Runnable() {
            public void run() {
                getSaleItemList();

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

    private void getSaleItemList() {
        try {
            // { "method":"getSaleItem" , "info":{"name":"lsj", "id":1 }}
            JSONObject ob = new JSONObject();
            ob.put("method", "getItemList");

            JSONObject info = new JSONObject();
            info.put("name", "double");
            info.put("id", FromId);
            ob.put("info", info);

            Toast.makeText(this, ob.toString(), Toast.LENGTH_SHORT).show();

            obRet = getSaleItemListFromServer(ob);

        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject getSaleItemListFromServer(JSONObject ob)
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

    private void initSaleItem() throws JSONException {
        int retNum = obRet.getInt("num");
        JSONArray array = obRet.getJSONArray("array");
        for(int i = 0; i < retNum; i++) {
            JSONObject ob = array.getJSONObject(i);
            String type_name = ob.getString("type_name");
            Double price = ob.getDouble("price");
            int star = ob.getInt("star");

            int sourceId = R.mipmap.man_cloth;
            SaleItem item1 = new SaleItem(type_name, sourceId, price, star);
            saleList.add(item1);
        }

        SalesAdapter adapter = new SalesAdapter(ChildSaleList.this,
                R.layout.sale_item, saleList, this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
    }

    public void click(View v, int position) {
        Toast.makeText(this, "position = " + position,
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ChildSaleList.this, OriginPicCheck.class);
        intent.putExtra("extra_data", position);
        startActivity(intent);
    }
}
