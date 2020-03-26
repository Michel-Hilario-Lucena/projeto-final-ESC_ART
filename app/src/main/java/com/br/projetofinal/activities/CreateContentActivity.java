package com.br.projetofinal.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.projetofinal.R;
import com.br.projetofinal.models.Post;
import com.br.projetofinal.utils.MySystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateContentActivity extends AppCompatActivity {
    private ImageView imageView;
    private final int imageRequestCode = 1;
    private final int galleryRequestCode = 2;
    private String resultPhoto;
    private byte[] resultGallery;

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
        final ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout3);
        final EditText title = findViewById(R.id.create_content_title);
        final EditText text = findViewById(R.id.create_content_text);
        final Button createButton = findViewById(R.id.create_content_button);
        final Button openCamera = findViewById(R.id.create_content_open_camera);

        imageView = findViewById(R.id.create_content_imageView);

        constraintLayout.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                float radius = 25 * Resources.getSystem().getDisplayMetrics().density;
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        constraintLayout.setClipToOutline(true);

        addImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                openGallery();
            else
                Toast.makeText(getApplicationContext(), "Não há permissão para iniciar a galeria", Toast.LENGTH_SHORT).show();
        });
        openCamera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission
                    (this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                initCamera();
            else
                Toast.makeText(getApplicationContext(), "Não há permissão para iniciar a camera", Toast.LENGTH_SHORT).show();
        });


        createButton.setOnClickListener(view -> {
            final Post createdPost;
            if (resultPhoto != null) {
                final File file = new File(resultPhoto);
                createdPost = new Post(file.getName(), title.getText().toString(), text.getText().toString());
                MySystem.saveImageFile(file);
                MySystem.createPost(createdPost);
            } else if (resultGallery != null) {
                final String name = "IMG" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
                createdPost = new Post(name, title.getText().toString(), text.getText().toString());
                MySystem.saveImageBytes(resultGallery, name);
                MySystem.createPost(createdPost);
            }
            finish();
        });
        setSupportActionBar(findViewById(R.id.create_content_toolbar));
    }

    private void openGallery() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/");
        final String[] mimeType = {"image/jpeg", "image/png"};
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        startActivityForResult(galleryIntent, galleryRequestCode);

    }

    private void initCamera() {
        final Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            final File imageFile;
            try {
                final String currentNameFile = "IMG" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
                final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                imageFile = File.createTempFile(currentNameFile, ".jpg", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            resultPhoto = imageFile.getAbsolutePath();
            final Uri uri = FileProvider.getUriForFile
                    (getApplicationContext(), "com.br.projetofinal.fileprovider", imageFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(pictureIntent, imageRequestCode);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case imageRequestCode:
                    resultGallery = null;
                    final File image = new File(resultPhoto);
                {
                    final Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    final Uri contentUri = Uri.fromFile(image);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                }
                try (FileInputStream fis = new FileInputStream(image)) {
                    final Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
                case galleryRequestCode:
                    resultPhoto = null;
                    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        bitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);
                        resultGallery = stream.toByteArray();
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
    }
}
