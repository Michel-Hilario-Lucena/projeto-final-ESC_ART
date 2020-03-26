package com.br.projetofinal.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.br.projetofinal.R;
import com.br.projetofinal.activities.NavigationActivity;
import com.br.projetofinal.interfaces.OnImageCallBack;
import com.br.projetofinal.interfaces.OnPostsCallBack;
import com.br.projetofinal.interfaces.OnUserCallBack;
import com.br.projetofinal.models.AbstractUser;
import com.br.projetofinal.models.Common;
import com.br.projetofinal.models.Post;
import com.br.projetofinal.models.Teacher;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class MySystem {
    private static final String TAG_PATH_COMMON = AbstractUser.TAG + "/collection_" + Common.TAG + "/" + Common.TAG + "/";
    private static final String TAG_PATH_TEACHER = AbstractUser.TAG + "/collection_" + Teacher.TAG + "/" + Teacher.TAG + "/";
    private static final String TAG_PATH_POST = "post";

    public static void registerUser(AbstractUser user, String password, Activity activity) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(taskComplete -> {
            if (!taskComplete.isSuccessful()) return;
            final FirebaseFirestore ff = FirebaseFirestore.getInstance();
            final String id = Base64.encodeToString(user.getEmail().getBytes(), Base64.URL_SAFE);
            final String tagUser = (user instanceof Teacher ? Teacher.TAG : Common.TAG);
            user.setCreatedAt(Timestamp.now());
            ff.document(AbstractUser.TAG + "/" + "collection_" + tagUser + "/" + tagUser + "/" + id).set(user)
                    .addOnSuccessListener(
                            command -> {
                                activity.startActivity
                                        (new Intent(activity, NavigationActivity.class));
                                activity.finish();
                            }
                    );
        });
    }

    public static void getThisUser(OnUserCallBack onUserCallBack) {
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onUserCallBack.onResultUser(null);
            return;
        }
        final String email = Base64.encodeToString(
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getEmail()
                        .getBytes(), Base64.URL_SAFE);

        ff.document(TAG_PATH_COMMON + email).get()
                .addOnSuccessListener(docCommon -> {
                    if (docCommon.exists())
                        onUserCallBack.onResultUser(docCommon.toObject(Common.class));
                    else ff.document(TAG_PATH_TEACHER + email).get()
                            .addOnSuccessListener(docTeacher -> onUserCallBack.onResultUser(docTeacher.toObject(Teacher.class)));
                });
    }

    public static void getUserByEmail(String email, OnUserCallBack onUserCallBack) {
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        final String encodedEmail = Base64.encodeToString(email.getBytes(), Base64.URL_SAFE);

        ff.document(TAG_PATH_COMMON + encodedEmail).get()
                .addOnSuccessListener(docCommon -> {
                    if (docCommon.exists())
                        onUserCallBack.onResultUser(docCommon.toObject(Common.class));
                    else ff.document(TAG_PATH_TEACHER + encodedEmail).get()
                            .addOnSuccessListener(docTeacher ->
                                    onUserCallBack.onResultUser(docCommon.toObject(Teacher.class)));
                });
    }

    public static void loginUser(String emailString, String passwordString, Activity activity) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailString, passwordString)
                .addOnSuccessListener(
                        execute -> getThisUser(user -> {
                            activity.startActivity(new Intent(activity, NavigationActivity.class));
                            activity.finish();
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    activity.findViewById(R.id.login_email).setBackground(activity.getDrawable(R.drawable.background_editext_error));
                    activity.findViewById(R.id.login_password).setBackground(activity.getDrawable(R.drawable.background_editext_error));
                });
    }

    public static void createPost(Post post) {
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection(TAG_PATH_POST).document().set(post).addOnCompleteListener(command -> {
            if (command.isSuccessful()) Log.i("TESTE", "Sucesso ao criar post");
            else Log.i("TESTE", "falha:" + command.getException().getMessage());
        });
    }

    public static void getAllPost(OnPostsCallBack onPostsCallBack) {
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection(MySystem.TAG_PATH_POST).get().addOnSuccessListener(command -> {
            final List<Post> posts = new ArrayList<>();
            for (DocumentSnapshot ds : command.getDocuments()) posts.add(ds.toObject(Post.class));
            onPostsCallBack.result(posts);
        });
    }

    public static void saveImageFile(File file) {
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference()
                .child(file.getName())
                .putFile(Uri.fromFile(file))
                .addOnCompleteListener(command -> Log.i("TESTE", command.isSuccessful() ? "sucesso" : command.getException().getMessage()));
    }

    public static void saveImageBytes(byte[] bytes, String name) {
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference()
                .child(name)
                .putBytes(bytes)
                .addOnCompleteListener(command -> Log.i("TESTE", command.isSuccessful() ? "sucesso" : command.getException().getMessage()));
    }

    public static void getImageIn(String path, OnImageCallBack onImageCallBack) {
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final long max_3MB = 3_000_072;
        firebaseStorage.getReference(path).getBytes(max_3MB)
                .addOnSuccessListener(
                        bytes -> onImageCallBack.resultImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length))
                );
    }
}
