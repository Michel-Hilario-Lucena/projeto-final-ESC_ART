package com.br.projetofinal.models;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

public abstract class AbstractUser {
    public static final String TAG="usuario";
    private String name;
    private String email;
    private Timestamp createdAt;
    private String typeUser;
    private Bitmap imageProfile;
    public AbstractUser(){}
    public AbstractUser (String name,String email){
        this.name=name;
        this.email=email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public Bitmap getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(Bitmap imageProfile) {
        this.imageProfile = imageProfile;
    }
}

