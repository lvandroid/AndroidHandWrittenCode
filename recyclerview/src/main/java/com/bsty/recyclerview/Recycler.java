package com.bsty.recyclerview;

import android.view.View;

import java.util.Stack;

public class Recycler {
    //集合 ===> list stack数组
    private Stack<View>[] views;
    public Recycler(int count) {
        views = new Stack[count];
        for(int i = 0; i < count; i++){
            views[i] = new Stack<>();
        }
    }

    //取
    public View getRecycledView(int type){
        try {
            return views[type].pop();
        }catch (Exception e){
            return null;
        }
    }

    //存
    public void addRecycledView(View view ,int type){
        views[type].push(view);
    }
}
