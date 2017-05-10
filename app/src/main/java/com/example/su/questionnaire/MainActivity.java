package com.example.su.questionnaire;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View;
import android.view.ViewGroup;


import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";
    public String path = "http://140.113.123.248:3000/questionnaire";
    public String address;
    public TextView text;
    private CheckBox inside;
    private CheckBox techOrange;
    private CheckBox medium;
    private CheckBox beauty;
    private CheckBox joke;
    private CheckBox stupid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        address = getBluetoothMacAddress(this);
        text = (TextView)findViewById(R.id.bluetooth);
        text.setText(address);
        inside = (CheckBox) findViewById(R.id.inside);
        techOrange = (CheckBox) findViewById(R.id.techOrange);
        medium = (CheckBox) findViewById(R.id.medium);
        beauty = (CheckBox) findViewById(R.id.pttBeauty);
        joke = (CheckBox) findViewById(R.id.pttJoke);
        stupid = (CheckBox) findViewById(R.id.pttStupid);
        Button btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        });
    }
    public static String getBluetoothMacAddress(Context mContext) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // BluetoothAdapter.getDefaultAdapter().DEFAULT_MAC_ADDRESS;
        // if device does not support Bluetooth
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "device does not support bluetooth");
            return null;
        }
        String address = mBluetoothAdapter.getAddress();
        if (address.equals("02:00:00:00:00:00")) {
            try {
                ContentResolver mContentResolver = mContext.getContentResolver();
                address = Settings.Secure.getString(mContentResolver, SECURE_SETTINGS_BLUETOOTH_ADDRESS);
            } catch (Exception e) {

            }
        }
        return address;
    }
    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject json = new JSONObject();
                JSONArray arr = new JSONArray();

                json.put("bluetooth_id", address);

                EditText nickName = (EditText) findViewById(R.id.nickName);
                json.put("nickName", nickName.getText());

                EditText birthday = (EditText) findViewById(R.id.birthday);
                json.put("birthday", birthday.getText());

                if (((RadioButton) findViewById(R.id.bachelor)).isChecked()) {
                    json.put("occupation", "bachelor");
                } else if (((RadioButton) findViewById(R.id.masterDr)).isChecked()){
                    json.put("occupation", "masterDr");
                }else{
                    json.put("occupation", "faculty");
                }
                if (inside.isChecked()) {
                    arr.put("inside");
                }
                if (techOrange.isChecked()) {
                    arr.put("techOrange");
                }
                if (medium.isChecked()) {
                    arr.put("medium");
                }
                if (beauty.isChecked()) {
                    arr.put("pttBeauty");
                }
                if (joke.isChecked()) {
                    arr.put("pttJoke");
                }
                if (stupid.isChecked()) {
                    arr.put("pttStupid");
                }
                json.put("user_preference", arr);

                text.setText(json.toString());
                text.setText(sendUserPreference(path, json));
            } catch (Exception e){
                text.setText("Error occurred!");
            }

            return;
        }
    };
    public String sendUserPreference(String path, JSONObject msg){
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            OutputStream os= conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeBytes(msg.toString());
            dos.flush();
            dos.close();
            if(conn.getResponseCode() == 200){
                return "Success";
            }else{
                return "Failed";
            }
        }catch (Exception e){
            return "Yo";
        }
    }
}
