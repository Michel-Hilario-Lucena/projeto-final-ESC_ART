package com.br.projetofinal.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.projetofinal.R;
import com.br.projetofinal.models.Post;
import com.br.projetofinal.utils.CaptureImage;
import com.br.projetofinal.utils.MySystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateContentActivity extends AppCompatActivity {
    private ImageView imageView;


    public CreateContentActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? Manifest.permission.ACCESS_MEDIA_LOCATION : ""
            }, 0);

        final Button addImage = findViewById(R.id.create_content_add_image);
        final EditText title = findViewById(R.id.create_content_title);
        final EditText text = findViewById(R.id.create_content_text);
        final Button createButton = findViewById(R.id.create_content_button);
        final Button openCamera = findViewById(R.id.create_content_open_camera);

        imageView = findViewById(R.id.create_content_imageView);

        addImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                CaptureImage.openGallery(this);
            else
                Toast.makeText(getApplicationContext(), "Não há permissão para iniciar a galeria", Toast.LENGTH_SHORT).show();
        });
        openCamera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                CaptureImage.openCamera(this);
            else
                Toast.makeText(getApplicationContext(), "Não há permissão para iniciar a camera", Toast.LENGTH_SHORT).show();
        });


        createButton.setOnClickListener(view -> {
            final Post createdPost;
            if (CaptureImage.resultPhoto != null) {
                final File file = new File(CaptureImage.resultPhoto);
                createdPost = new Post(file.getName(), title.getText().toString(), text.getText().toString());
                MySystem.saveImageFile(file);
                MySystem.createPost(createdPost);
            } else if (CaptureImage.resultGallery != null) {
                @SuppressLint("SimpleDateFormat")
                final String name = "IMG" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
                createdPost = new Post(name, title.getText().toString(), text.getText().toString());
                MySystem.saveImageBytes(CaptureImage.resultGallery, name);
                MySystem.createPost(createdPost);
            }
            finish();
        });
        setSupportActionBar(findViewById(R.id.create_content_toolbar));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CaptureImage.inActivityResult(resultCode, requestCode, data, this, imageView);
    }
}
