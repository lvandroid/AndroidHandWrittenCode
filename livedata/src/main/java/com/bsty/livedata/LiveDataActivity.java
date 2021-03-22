package com.bsty.livedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;

/**
 * LiveData  是一个数据持有类，它肯定会持有一个Object类型的数据
 * 它能够感知生命周期
 * 它能够有观察者，数据发生变化时，能通知观察者，能持有多个观察者
 */
public class LiveDataActivity extends AppCompatActivity {
    MutableLiveData<String> name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);
        name = new MutableLiveData<>();
        name.postValue("李三");

        name.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }
}