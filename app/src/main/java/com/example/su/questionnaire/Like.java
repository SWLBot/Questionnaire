package com.example.su.questionnaire;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;
import android.widget.TextView;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by su on 2017/5/14.
 */

public class Like extends AppCompatActivity {
    private String path="http://www.google.com";
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like);
        LikeButton lb =(LikeButton) findViewById(R.id.star_button);
        lb.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Thread thread = new Thread(mutiThread);
                thread.start();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        tv = (TextView) findViewById(R.id.textView);
    }
    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                if(conn.getResponseCode() == 200){
//                    tv.setText("Success");
                }else{
//                    tv.setText("Failed");
                }
            }catch (Exception e){
                tv.setText(e.toString());
            }
            return;
        }
    };
}
