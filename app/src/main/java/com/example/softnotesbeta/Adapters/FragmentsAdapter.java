package com.example.softnotesbeta.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentsAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<>();

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment fragment) {
        fragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
