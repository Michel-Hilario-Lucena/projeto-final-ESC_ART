package com.br.projetofinal.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.br.projetofinal.fragments.LoginFragment;
import com.br.projetofinal.fragments.RegisterFragment;

public class ViewPager2Adapter extends FragmentStateAdapter  {

    private final Fragment[]fragments;

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity,Fragment[]fragments) {
        super(fragmentActivity);
        this.fragments=fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
