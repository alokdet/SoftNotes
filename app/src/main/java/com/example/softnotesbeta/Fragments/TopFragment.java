package com.example.softnotesbeta.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softnotesbeta.LauncherActivity;
import com.example.softnotesbeta.R;

import java.util.ArrayList;

public class TopFragment extends Fragment {

    private ViewPager2 viewPager;
    private FragmentSlideAdapter fragmentSlideAdapter;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager2) view.findViewById(R.id.nested_viewpager);
        fragmentSlideAdapter = new FragmentSlideAdapter(getChildFragmentManager(), getLifecycle());
        fragmentSlideAdapter.addFragment(new ToolsFragment());
        fragmentSlideAdapter.addFragment(new ApplicationFragment());
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(fragmentSlideAdapter);
        viewPager.setCurrentItem(1);
    }

    public class FragmentSlideAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        public FragmentSlideAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}