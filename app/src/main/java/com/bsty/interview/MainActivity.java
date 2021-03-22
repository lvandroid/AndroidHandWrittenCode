package com.bsty.interview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsty.recyclerview.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new MyAdapter(this));
        //内存泄漏 handler bitmap cursor io
        //解决方案： softreference weakreference static
//        handler.sendEmptyMessageDelayed(HANDLER_FLAG,1000);
    }

    //    private int HANDLER_FLAG = 666;
//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            handler.sendEmptyMessageDelayed(HANDLER_FLAG,1000);
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        handler.removeMessages(HANDLER_FLAG);
//    }
    class MyAdapter implements RecyclerView.Adapter {
        private int height;
        LayoutInflater inflater;

        public MyAdapter(Context context) {
            Resources resources = context.getResources();
            height = resources.getDimensionPixelSize(R.dimen.item_height);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View onCreateViewHolder(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_image, viewGroup, false);
            }
            return convertView;
        }

        @Override
        public View onBinderViewHolder(int position, View convertView, ViewGroup viewGroup) {
            TextView textView = convertView.findViewById(R.id.textview);
            textView.setText("第" + position + "行");
            return convertView;
        }

        @Override
        public int getCount() {
            return 500000;
        }

        @Override
        public int getItemViewType(int row) {
            return 1;
        }

        @Override
        public int getItemTypeCount() {
            return 1;
        }

        @Override
        public int getHeight(int position) {
            return 100;
        }
    }
}