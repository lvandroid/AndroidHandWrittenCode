package com.bsty.recyclerview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView extends ViewGroup {
    private boolean needRelayout;
    private List<View> viewList;
    private Adapter adapter;
    private int[] heights;
    private int width;
    private int height;
    private Recycler recycler;
    private int touchSlop;

    private void init(Context context) {
        needRelayout = true;
        viewList = new ArrayList<>();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        //获取最小滑动距离
        touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            needRelayout = true;
            this.adapter = adapter;
            recycler = new Recycler(adapter.getItemTypeCount());
        }

    }

    public RecyclerView(Context context) {
        super(context);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int rowCount;

    /**
     * 重新摆放子控件
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed) {
            //测量
            needRelayout = false;
            //摆放的时候,初始化
            viewList.clear();
            //比较耗时间
            removeAllViews();
            if (adapter != null) {
                rowCount = adapter.getCount();
                //高度
                heights = new int[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    //依赖这个方法测量item的高度
                    heights[i] += adapter.getHeight(i);

                }
                width = r - l;
                height = b - t;
                int top = 0, bottom;
                for (int i = 0; i < rowCount && top < height; i++) {
                    bottom = top + heights[i];
                    //实例化 布局
                    //怎么摆放
                    //摆放多少个
                    View view = makeAndSetup(i, l, t, r, b);
                    viewList.add(view);
                    top = bottom;
                }
            }
        }
    }

    private View makeAndSetup(int indexData, int left, int top, int right, int bottom) {
        View view = obtain(indexData, right - left, bottom - top);
        view.layout(left, top, right, bottom);
        return view;
    }

    private View obtain(int row, int width, int height) {
        int type = adapter.getItemViewType(row);
        View recycledView = recycler.getRecycledView(type);
        //取不出来
        View view = null;
        if (recycledView == null) {
            view = adapter.onCreateViewHolder(row, null, this);
            if (view == null) {
                throw new RuntimeException("onCreateViewHolder 必须初始化");
            }
        } else {
            view = adapter.onBinderViewHolder(row, null, this);
        }
        //tag值， 填充，移除
        view.setTag(R.id.tag_type_view, type);
        //测量
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);
        return view;
    }

    private int currentY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                currentY = (int) ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int y2 = Math.abs(currentY - (int) ev.getRawY());
                if (y2 > touchSlop) {
                    intercept = true;
                }
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y2 = (int) event.getRawY();
                int diff = currentY - y2;
                scrollBy(0,diff);
        }
        return super.onTouchEvent(event);
    }

    private int scrollY;
    @Override
    public void scrollBy(int x, int y) {
        scrollY += y;
        repositionViews();
    }

    private void repositionViews() {
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        int type = (int) view.getTag(R.id.tag_type_view);
        recycler.addRecycledView(view, type);
    }

    public interface Adapter {
        View onCreateViewHolder(int position, View convertView, ViewGroup viewGroup);

        View onBinderViewHolder(int position, View convertView, ViewGroup viewGroup);

        int getCount();

        int getItemViewType(int row);

        int getItemTypeCount();

        int getHeight(int position);
    }

}
