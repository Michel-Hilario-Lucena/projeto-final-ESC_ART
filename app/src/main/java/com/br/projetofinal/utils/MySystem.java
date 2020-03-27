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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;

public abstract class MySystem {
    private static final String TAG_PATH_COMMON = AbstractUser.TAG + "/collection_" + Common.TAG + "/" + Common.TAG + "/";
    private static final String TAG_PATH_TEACHER = AbstractUser.TAG + "/collection_" + Teacher.TAG + "/" + Teacher.TAG + "/";
    private static final String TAG_PATH_POST = "post/" + getEncodedEmail() + "/posts_" + getEncodedEmail() + "/";
    public static final String PROF_NAME_IMG = "profile_img_" + MySystem.getEmail();

    public static void registerUser(AbstractUser user, String password, Activity activity) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(taskComplete -> {
            if (!taskComplete.isSuccessful()) return;
            final FirebaseFirestore ff = FirebaseFirestore.getInstance();
            final String tagUser = (user instanceof Teacher ? Teacher.TAG : Common.TAG);
            user.setCreatedAt(Timestamp.now());
            ff.document(AbstractUser.TAG + "/" + "collection_" + tagUser + "/" + tagUser + "/" + getEncodedEmail()).set(user)
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
        ff.document(TAG_PATH_COMMON + getEncodedEmail()).get()
                .addOnSuccessListener(docCommon -> {
                    if (docCommon.exists())
                        onUserCallBack.onResultUser(docCommon.toObject(Common.class));
                    else ff.document(TAG_PATH_TEACHER + getEncodedEmail()).get()
                            .addOnSuccessListener(docTeacher -> onUserCallBack.onResultUser(docTeacher.toObject(Teacher.class)));
                });
    }

    public static void getUserByEmail(String email, OnUserCallBack onUserCallBack) {
        final FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.document(TAG_PATH_COMMON + getEncodedEmail()).get()
                .addOnSuccessListener(docCommon -> {
                    if (docCommon.exists())
                        onUserCallBack.onResultUser(docCommon.toObject(Common.class));
                    else ff.document(TAG_PATH_TEACHER + getEncodedEmail()).get()
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

        ff.collection(TAG_PATH_COMMON).get().addOnSuccessListener(common ->
                ff.collection(TAG_PATH_TEACHER).get().addOnSuccessListener(teacher -> {
                    ArrayList<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot qds : common)
                        ff.collection(getPostOf(qds.getId())).get().addOnSuccessListener(command -> {
                                    for (DocumentSnapshot ds : command) posts.add(ds.toObject(Post.class));
                                    for (DocumentSnapshot docs : teacher)
                                        ff.collection(getPostOf(docs.getId())).get().addOnSuccessListener(command_ -> {
                                                    for (DocumentSnapshot ds : command_)
                                                        posts.add(ds.toObject(Post.class));
                                                    onPostsCallBack.result(posts);
                                                }
                                        );
                                }
                        );

                }));
    }

    public static void saveImageFile(File file) {
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference()
                .child(file.getName())
                .putFile(Uri.fromFile(file));
    }

    public static void saveImageBytes(byte[] bytes, String name) {
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.getReference()
                .child(name)
                .putBytes(bytes);
    }

    public static void getImageIn(String path, OnImageCallBack onImageCallBack) {
        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final long max3MB = 3_000_072;
        firebaseStorage.getReference(path).getBytes(max3MB)
                .addOnSuccessListener(
                        bytes -> onImageCallBack.resultImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length))
                );
    }

    private static String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    private static String getPostOf(String email) {
        return "post/" + getEncodedEmail() + "/posts_" + getEncodedEmail() + "/";
    }

    private static String getEncodedEmail() {
        return Base64.encodeToString(MySystem.getEmail().getBytes(), Base64.URL_SAFE);
    }

    private static String encodeEmail(String email) {
        return Base64.encodeToString(email.getBytes(), Base64.URL_SAFE);
    }
}
