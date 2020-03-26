package com.br.projetofinal.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.br.projetofinal.R;
import com.br.projetofinal.utils.EditTextManager;
import com.br.projetofinal.utils.MySystem;

public class LoginFragment extends Fragment {
    private Drawable background;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText email = view.findViewById(R.id.login_email);
        final EditText password = view.findViewById(R.id.login_password);
        final Button button = view.findViewById(R.id.login_button);
        button.setOnClickListener(v -> {
            final String emailString = email.getText().toString();
            final String passwordString = password.getText().toString();
            MySystem.loginUser(emailString, passwordString, getActivity());
        });
        return view;
    }

}
