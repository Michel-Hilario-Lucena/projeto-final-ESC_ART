package com.br.projetofinal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.br.projetofinal.R;
import com.br.projetofinal.utils.CaptureImage;
import com.br.projetofinal.utils.MySystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class SettingsActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.toolbar2));
        imageView = findViewById(R.id.imageView4);
        MySystem.getImageIn(MySystem.PROF_NAME_IMG, bitmap -> imageView.setImageBitmap(bitmap));
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CaptureImage.inActivityResult(resultCode, requestCode, data, this, imageView);
        if (CaptureImage.resultGallery != null)
            MySystem.saveImageBytes(CaptureImage.resultGallery, MySystem.PROF_NAME_IMG);
        else if (CaptureImage.resultPhoto != null && !CaptureImage.resultPhoto.isEmpty()) {
            final File file = new File(CaptureImage.resultPhoto);
            try (final FileInputStream fis = new FileInputStream(file)) {
                byte[] bytes = new byte[(int) file.length()];
                fis.read(bytes);
                MySystem.saveImageBytes(bytes, MySystem.PROF_NAME_IMG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
