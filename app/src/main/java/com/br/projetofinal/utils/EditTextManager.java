package com.br.projetofinal.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.br.projetofinal.interfaces.AfterTextListener;

public abstract class EditTextManager {
    public static void runOnChangedText( EditText editText,Runnable runnable) {
        new Thread(() ->
                editText.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        runnable.run();
                    }
                    @Override public void afterTextChanged(Editable s) {}
                })).start();
    }
    public static void addAfterTextChangedListener(EditText editText, AfterTextListener afterTextListener) {
        new Thread(() ->
                editText.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        afterTextListener.textChanged(s);
                    }
                })).start();
    }
}
