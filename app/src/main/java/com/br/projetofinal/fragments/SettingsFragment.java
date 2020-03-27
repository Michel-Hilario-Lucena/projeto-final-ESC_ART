package com.br.projetofinal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.br.projetofinal.R;
import com.br.projetofinal.utils.CaptureImage;
import com.br.projetofinal.utils.MySystem;

import java.io.File;


public class SettingsFragment extends AppCompatActivity {
    private ImageView imageView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        setSupportActionBar(findViewById(R.id.toolbar2));
        imageView = findViewById(R.id.imageView4);
        MySystem.getImageIn(MySystem.PROF_NAME_IMG,bitmap -> imageView.setImageBitmap(bitmap));
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding((int) (-11 * getResources().getDisplayMetrics().density), 0, 0, 0);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_prof_img_camera:
                    CaptureImage.openCamera(this, MySystem.PROF_NAME_IMG);
                    break;
                case R.id.menu_prof_img_gallery:
                    CaptureImage.openGallery(this);
            }
            return false;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CaptureImage.inActivityResult(resultCode, requestCode, data, this, imageView);
        if (CaptureImage.resultGallery != null)
            MySystem.saveImageBytes(CaptureImage.resultGallery, MySystem.PROF_NAME_IMG);
        else if (CaptureImage.resultPhoto != null && !CaptureImage.resultPhoto.isEmpty())
            MySystem.saveImageFile(new File(CaptureImage.resultPhoto));
    }
}
