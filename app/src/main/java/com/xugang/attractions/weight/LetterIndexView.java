package com.xugang.attractions.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xugang.attractions.R;

/**
 * Created by ASUS on 2016-09-29.
 */
public class LetterIndexView extends View {
    private String[] letters = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final String TAG = "test";
    private Paint paint;
    private int height;
    private int weight;
    private String letter = null;
    private String strings;

    public LetterIndexView(Context context) {
        this(context, null, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.drawable.shape);
        paint = new Paint();
        paint.setAntiAlias(true);           //抗锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (weight == 0) {
            height = getHeight();
            weight = getWidth();
        }
        paint.setTextSize(40);
        paint.setStrokeWidth(3);
        for (int i = 0; i < letters.length; i++) {
            String letter = letters[i];
            float letterSize = paint.measureText(letter);
            float startX = (weight - letterSize) / 2;

            float unitHetght = (height - 40) / 26f;
            float startY = 20 + i * unitHetght + (unitHetght - letterSize) / 2 + letterSize;

            if (letters[i].equals(this.letter)) {
                paint.setColor(Color.RED);
                canvas.drawText(letter, startX, startY, paint);
            } else {
                paint.setColor(Color.BLACK);
                canvas.drawText(letter, startX, startY, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (callback != null)
                    callback.onTouch(true);
                setAuto(y);
                break;
            case MotionEvent.ACTION_MOVE:
                setAuto(y);

                break;
            case MotionEvent.ACTION_UP:
                if (callback != null)
                    callback.onTouch(false);
                setBackgroundResource(R.drawable.shape);
                break;
        }
        return true;
    }

    private void setAuto(float y) {
        if (y / height >= 0 && y / height <= letters.length) {
            //设置高亮
            setBackgroundResource(R.drawable.shape_pressed);
            //根据y的位置动态计算当前位于哪个字母;
            int i = (int) ((y / (float) getHeight()) * letters.length);
            letter = letters[i];
            if (callback != null)
                callback.onPositionChanged(letter);
//            Intent intent = new Intent(MyUtil.ACTION_LETTER);
//            intent.putExtra("letter", letter);
//            getContext().sendBroadcast(intent);
            invalidate();
        }
    }

    LetterIndexViewCallBack callback;

    public void setLetterIndexView(LetterIndexViewCallBack callback) {
        this.callback = callback;
    }

    public void setCurrentLetter(String firstLetter) {
        for (int i = 0; i < letters.length; i++) {
            if (letters[i].equals(firstLetter)) {
                letter = firstLetter;
                invalidate();
                return;
            }
        }
    }

    public interface LetterIndexViewCallBack {
        void onPositionChanged(String letter);

        void onTouch(boolean flag);
    }
}


