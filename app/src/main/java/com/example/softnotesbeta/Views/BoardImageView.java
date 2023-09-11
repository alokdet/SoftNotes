package com.example.softnotesbeta.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class BoardImageView extends AppCompatImageView {

    private Paint drawPaint;
    private int paintColor = 0xFF660000;
    private float mX, mY;

    public BoardImageView(@NonNull Context context) {
        super(context);

        init();
    }

    public BoardImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getX(), getY(), getX() + getMeasuredWidth(), getY() + getMeasuredHeight(), drawPaint);
        canvas.drawRect(getX(), (getMeasuredHeight() / 4), (getX() + 20), (getMeasuredHeight() - (getMeasuredHeight() / 4)), drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = touchX;
                mY = touchY;

                break;
            case MotionEvent.ACTION_MOVE:
                float dX = Math.abs(touchX - mX);
                float dY = Math.abs(touchY - mY);

                setTranslationX(dX);
                setTranslationY(dY);

                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                return false;
        }
        return true;
    }

    private void init() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(12);
        drawPaint.setStyle(Paint.Style.STROKE);
    }
}
