package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewpager.widget.PagerAdapter;

import com.example.softnotesbeta.R;

import java.util.List;

public class TextPagerAdapter extends PagerAdapter {

    private Context context;
    private List<CharSequence> pages;

    public TextPagerAdapter(Context context, List<CharSequence> pages) {
        this.context = context;
        this.pages = pages;
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
        View view = LayoutInflater.from(context).inflate(R.layout.text_page_layout, container, false);
        AppCompatEditText editText = (AppCompatEditText) view.findViewById(R.id.writer_input);

        CharSequence text = pages.get(position);
        editText.setText(text);

        container.addView(view, position);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
