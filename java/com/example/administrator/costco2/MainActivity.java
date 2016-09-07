package com.example.administrator.costco2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends Activity implements View.OnClickListener{
    private EditText edit_account;
    private EditText edit_passwd;
    private Button   button_login;
    private Handler  handler;

    Socket socket = null;
    String buffer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_account = (EditText) findViewById(R.id.account);
        edit_passwd = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.login);
        button_login.setOnClickListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1 : {
                        Toast.makeText(MainActivity.this,
                                "ret = -1", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 0 : {
                        Toast.makeText(MainActivity.this, "ret = 0", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, AllSaleList.class);
                        startActivity(intent);
                        break;
                    }
                    default: {
                        Toast.makeText(MainActivity.this, "ret = " + msg.what, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login: {
                new Thread(new Runnable() {
                    public void run() {
                        Message message = new Message();
                        // message.what = checkLogin();
                        message.what = 0; //checkLogin();
                        handler.sendMessage(message); // 将Message对象发送出去
                    }
                }).start();
                break;
            }
            default:
                break;
        }
    }

    public int checkLogin() {
        Editable account_value = edit_account.getText();
        Editable passwd_value = edit_passwd.getText();
        if(account_value.length() < 1 || passwd_value.length() < 1) {
            // return 0;
            return -1;
        }
        else {
            try {
                // {"method":"CheckLogin", "info":{"account":"double", "passwd":"XXX"}}
                JSONObject ob = new JSONObject();
                ob.put("method", "CheckLogin");

                JSONObject info = new JSONObject();
                info.put("account", account_value.toString());
                info.put("passwd", passwd_value.toString());

                ob.put("info", info);

                return sendLoginInfo2Server(ob);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public int sendLoginInfo2Server(JSONObject ob)
    {
        try {
            socket = new Socket("192.168.1.106", 8009);
            OutputStream ou = socket.getOutputStream();
            ou.write(ob.toString().getBytes());
            ou.flush();

            InputStream input = socket.getInputStream();
            byte[]  bs = new byte[1024];
            int i = -1;
            while((i = input.read(bs)) != -1) {
                return getLoginRet(new String(bs));
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

        return -1;
    }

    public int getLoginRet(String retInfo) {
        try {
            JSONObject jsonObject = new JSONObject(retInfo);
            return jsonObject.getInt("ret");
        }
        catch (JSONException e) {
            return -1;
        }
    }
}
