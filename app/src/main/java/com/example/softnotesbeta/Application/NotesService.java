package com.example.softnotesbeta.Application;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.softnotesbeta.MainActivity;
import com.example.softnotesbeta.R;

public class NotesService extends Service {

    private ViewGroup floatingView;
    private AppCompatImageView exitFloatingView;
    private AppCompatTextView noteView;

    private WindowManager.LayoutParams floatingWindowLayoutParams;
    private WindowManager windowManager;

    private int LAYOUT_TYPE;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        ;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        floatingView = (ViewGroup) inflater.inflate(R.layout.floating_note_layout, null);

        exitFloatingView = (AppCompatImageView) floatingView.findViewById(R.id.exit_floating_window);
        noteView = (AppCompatTextView) floatingView.findViewById(R.id.note_text);

        noteView.setText(Common.text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }

        floatingWindowLayoutParams = new WindowManager.LayoutParams(
                width,
                (int) (height * (0.58f)),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.OPAQUE
        );

        floatingWindowLayoutParams.gravity = Gravity.CENTER;

        floatingWindowLayoutParams.x = 0;
        floatingWindowLayoutParams.y = 0;

        windowManager.addView(floatingView, floatingWindowLayoutParams);

        exitFloatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                windowManager.removeView(floatingView);

                Intent intent = new Intent(NotesService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        floatingView.setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams floatingWindowUpdatedLayoutParams = floatingWindowLayoutParams;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatingWindowUpdatedLayoutParams.x;
                        y = floatingWindowUpdatedLayoutParams.y;

                        px = event.getRawX();
                        py = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatingWindowUpdatedLayoutParams.x = (int) ((x + event.getRawX()) - px);
                        floatingWindowUpdatedLayoutParams.y = (int) ((y + event.getRawY()) - py);

                        windowManager.updateViewLayout(floatingView, floatingWindowUpdatedLayoutParams);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
        windowManager.removeView(floatingView);
    }
}
