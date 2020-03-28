package com.br.projetofinal.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.projetofinal.R;
import com.br.projetofinal.adapters.ViewPager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        final BottomNavigationView bNavMenu = view.findViewById(R.id.home_bootom_nav);
        bNavMenu.setSelectedItemId(R.id.menu_bottom_nav_home);
        final ViewPager2 viewPager2 = view.findViewById(R.id.home_viewPager);
        Fragment[] fragments = {new FeedFragment(), new HomeFragment(), new GalleryFragment()};
        viewPager2.setAdapter(new ViewPager2Adapter(this.requireActivity(), fragments));
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                bNavMenu.setSelectedItemId(
                        0 == position ? R.id.menu_bottom_nav_feed :
                                1 == position ? R.id.menu_bottom_nav_home :
                                        R.id.menu_bottom_nav_gallery);
            }
        });
        bNavMenu.setOnNavigationItemSelectedListener(item -> {
            viewPager2.setCurrentItem(
                    item.getItemId() == R.id.menu_bottom_nav_feed ? 0 :
                            item.getItemId() == R.id.menu_bottom_nav_home ? 1 : 2
            );
            return true;
        });
        return view;
    }
}
