package com.br.projetofinal.interfaces;

import com.br.projetofinal.models.Post;

import java.util.List;

@FunctionalInterface
public interface OnPostsCallBack {
    void result(List<Post> posts);
}
