package com.br.projetofinal.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.br.projetofinal.R;
import com.br.projetofinal.adapters.ViewPager2Adapter;
import com.br.projetofinal.fragments.LoginFragment;
import com.br.projetofinal.fragments.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, NavigationActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);
            final ViewPager2 viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(new ViewPager2Adapter(this,new Fragment[]{new LoginFragment(), new RegisterFragment()}));
        }
    }
}
