package com.br.projetofinal.interfaces;

import android.graphics.Bitmap;

@FunctionalInterface
public interface OnImageCallBack {
    void resultImage(final Bitmap bitmap);
}
