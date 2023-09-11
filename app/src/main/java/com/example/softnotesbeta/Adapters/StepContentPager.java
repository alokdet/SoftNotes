package com.example.softnotesbeta.Adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.softnotesbeta.Application.SoftScript.SoftScriptReader;
import com.example.softnotesbeta.Application.TaskService;
import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.R;

import java.util.List;

public class StepContentPager extends PagerAdapter {

    private Context context;
    private List<Step> pages;

    public StepContentPager(Context context, List<Step> stepList) {
        this.context = context;
        this.pages = stepList;

    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.step_content_page_layout, container, false);
        AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.main_content_view);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.content_container);

        Step step = pages.get(position);

        String text = step.getContent();
        textView.setText(text);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
