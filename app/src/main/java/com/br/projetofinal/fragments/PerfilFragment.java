package com.br.projetofinal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.br.projetofinal.R;

public class PerfilFragment extends Fragment {
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setPadding((int) (-11* getResources().getDisplayMetrics().density),0,0,0);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_prof_img_camera:
                    Toast.makeText(getContext(),"teste camera",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_prof_img_gallery:
                    Toast.makeText(getContext(),"teste galeria",Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        return view;
    }

}
