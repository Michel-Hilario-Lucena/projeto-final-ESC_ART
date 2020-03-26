package com.br.projetofinal.interfaces;

import com.br.projetofinal.models.AbstractUser;

@FunctionalInterface
public interface OnUserCallBack {
    void onResultUser(AbstractUser user);
}
