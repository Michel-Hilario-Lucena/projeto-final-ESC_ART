package com.br.projetofinal.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.projetofinal.R;
import com.br.projetofinal.adapters.PostAdapter;
import com.br.projetofinal.models.Post;
import com.br.projetofinal.utils.MySystem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_home,container,false);
        final RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        List<Post> posts=new ArrayList<>();
        final PostAdapter postAdapter =new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setHasFixedSize(false);
        MySystem.getAllPost(allPosts-> {
            posts.addAll(allPosts);
            postAdapter.notifyDataSetChanged();
            Log.i("TESTE","size:"+allPosts.size());
        });
        return view;
    }
}
