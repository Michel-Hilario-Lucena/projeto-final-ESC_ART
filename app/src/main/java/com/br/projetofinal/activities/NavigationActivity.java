package com.br.projetofinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.br.projetofinal.R;
import com.br.projetofinal.fragments.PerfilFragment;
import com.br.projetofinal.fragments.PessoasFragment;
import com.br.projetofinal.fragments.SettingsFragment;
import com.br.projetofinal.fragments.StartFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        final Toolbar toolbar = findViewById(R.id.home_teacher_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_inicio:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_host, new StartFragment()).commit();
                    break;
                case R.id.menu_item_perfil:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_host, new PerfilFragment()).commit();
                    break;
                case R.id.menu_item_pessoas:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_host, new PessoasFragment()).commit();
                    break;
                case R.id.menu_item_config:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_host, new SettingsFragment()).commit();
                    break;
                default:
                    FirebaseAuth.getInstance().signOut();
                    finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        navigationView.getHeaderView(0).findViewById(R.id.imageView3).setClipToOutline(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_host, new StartFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_item_inicio);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_home_create)
            startActivity(new Intent(this, CreateContentActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

}
