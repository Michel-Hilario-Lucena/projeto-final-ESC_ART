package com.br.projetofinal.interfaces;

import android.text.Editable;

@FunctionalInterface
public interface AfterTextListener {
    void textChanged(Editable s);
}
