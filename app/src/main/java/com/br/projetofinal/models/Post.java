package com.br.projetofinal.models;

import android.graphics.Bitmap;

public class Post {
    private String imageReference;
    private String title;
    private String text;

    public Post() {
    }

    public Post(String imageReference, String title, String text) {
        this.imageReference = imageReference;
        this.title = title;
        this.text = text;
    }

    public Post(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Post(String text) {
        this.text = text;
    }
    public String getImageReference() {return imageReference;}
    public void setImageReference(String imageReference) {this.imageReference = imageReference;}
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
