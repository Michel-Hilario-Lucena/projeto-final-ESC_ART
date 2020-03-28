package com.br.projetofinal.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureImage {
    private static final int imageRequestCode = 0xB0A;
    private static final int galleryRequestCode = 0xB0DE;
    public static String resultPhoto;
    public static byte[] resultGallery;


    public static void openCamera(Activity activity) {
        openCamera(activity, null);
    }

    public static void openCamera(Activity activity, String nameFile) {
        final Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            final File imageFile;
            try {
                @SuppressLint("SimpleDateFormat")
                final String currentNameFile = "IMG_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                final File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                imageFile = File.createTempFile(nameFile!=null?nameFile:currentNameFile, ".jpg", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            resultPhoto = imageFile.getAbsolutePath();
            final Uri uri = FileProvider.getUriForFile
                    (activity.getApplicationContext(), "com.br.projetofinal.fileprovider", imageFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(pictureIntent, imageRequestCode);
        }
    }

    public static void openGallery(Activity activity) {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/");
        final String[] mimeType = {"image/jpeg", "image/png"};
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        activity.startActivityForResult(galleryIntent, galleryRequestCode);
    }

    public static void inActivityResult(
            int resultCode,
            int requestCode,
            Intent data,
            Activity activity,
            ImageView imageView
    ) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case CaptureImage.imageRequestCode:
                    CaptureImage.resultGallery = null;
                    final File image = new File(CaptureImage.resultPhoto);
                {
                    final Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    final Uri contentUri = Uri.fromFile(image);
                    mediaScanIntent.setData(contentUri);
                    activity.sendBroadcast(mediaScanIntent);
                }
                try (FileInputStream fis = new FileInputStream(image)) {
                    final Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    imageView.setImageBitmap(bitmap);
                    Toast.makeText(activity, "deveria ir :/", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
                case CaptureImage.galleryRequestCode:
                    CaptureImage.resultPhoto = null;
                    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                activity.getContentResolver(), data.getData()
                        );
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                        CaptureImage.resultGallery = stream.toByteArray();
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
    }
}