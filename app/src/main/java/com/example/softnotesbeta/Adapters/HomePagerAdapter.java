package com.example.softnotesbeta.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.softnotesbeta.R;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends PagerAdapter {

    public HomePagerAdapter() {

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.home_header_layout;
                break;
            case 1:
                resId = R.id.carousel_parent;
                break;
            case 2:
                resId = R.id.instant_tasks_reminder;
                break;
        }
        return container.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }
}

