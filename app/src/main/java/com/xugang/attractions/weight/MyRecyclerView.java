package com.xugang.attractions.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ASUS on 2016-10-20.
 */
public class MyRecyclerView extends RecyclerView {
    private float downX;
    private float downY;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float xyRatio = 0;
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            downX = e.getX();
            downY = e.getY();
        }

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float x = e.getX();
            float y = e.getY();
            float dx = Math.abs(x - downX);
            float dy = Math.abs(y - downY);
            xyRatio = dx / dy;

            //手指Y方向没有明显移动，不给LV玩
            //手指移动为横移，不给LV玩
            if (dy < 50 || xyRatio > 1) {
//                Log.e("test", "self consume");
                return true;
            }
        }

        return super.onTouchEvent(e);
    }
}
