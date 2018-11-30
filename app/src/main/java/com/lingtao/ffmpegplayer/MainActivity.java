package com.lingtao.ffmpegplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lingtao.core.net.RestClient;
import com.lingtao.core.net.callback.ISuccess;
import com.lingtao.librarydemo.Utils;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getData() {

        HashMap<String, Object> parames = new HashMap<>();
        parames.put("key", "488c65f3230c0280757b50686d1f1cd5");
        parames.put("sort", "asc");
        parames.put("time", "1418816972");
        RestClient client = RestClient.create()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String responce) {
                        Log.d("nongyulian", responce);
                    }
                })
                .url("content/list.from")
                .params(parames)
                .build();
        client.get();

        Log.d("nongyulian", Utils.showLog());


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getData(View view) {
        getData();
//        RestClient.test();

    }
}
