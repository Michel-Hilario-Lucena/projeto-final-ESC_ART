package com.br.projetofinal.fragments;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.br.projetofinal.R;
import com.br.projetofinal.models.AbstractUser;
import com.br.projetofinal.models.Common;
import com.br.projetofinal.models.Teacher;
import com.br.projetofinal.utils.EditTextManager;
import com.br.projetofinal.utils.MySystem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;


public class RegisterFragment extends Fragment {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private TabLayout tabLayout;
    private AbstractUser currentUser;
    private boolean isCommom=true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        tabLayout=view.findViewById(R.id.register_select_type_user);
        name = view.findViewById(R.id.register_name);
        email = view.findViewById(R.id.register_email);
        password = view.findViewById(R.id.register_password);
        passwordConfirm = view.findViewById(R.id.register_password_confirm);

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener(){
            @Override public void onTabSelected(TabLayout.Tab tab) {
                isCommom=tab.getText().toString().equals(Common.TAG);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        EditTextManager.runOnChangedText(name, () -> {
            final String name = this.name.getText().toString();
            this.name.setBackground(getBackgroundOrDefaultOrOk(name.isEmpty()));
        });

        EditTextManager.runOnChangedText(email, () -> {
            final String email = this.email.getText().toString();
            this.email.setBackground(getBackgroundOrDefaultOrOk(email.isEmpty()));
        });

        final Runnable r = () -> {
            final String p1 = password.getText().toString();
            final String p2 = passwordConfirm.getText().toString();
            password.setBackground(getBackgroundOrDefaultOrOkOrError(p1.isEmpty() && p2.isEmpty(), p1.equals(p2)));
            passwordConfirm.setBackground(getBackgroundOrDefaultOrOkOrError(p1.isEmpty() && p2.isEmpty(), p1.equals(p2)));
        };

        EditTextManager.runOnChangedText(password, r);
        EditTextManager.runOnChangedText(passwordConfirm, r);

        final Button registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(viewClick -> {
            final String name=this.name.getText().toString();
            final String email=this.email.getText().toString();
            final String password=this.password.getText().toString();

            boolean isAnyFieldEmpty=false;
            for (EditText field : new EditText[]{this.name, this.email, this.password, passwordConfirm})
                if (field.getText().toString().isEmpty()) {
                    isAnyFieldEmpty=true;
                    field.setBackground(getResources().getDrawable(R.drawable.background_editext_error));
                }
            if (isAnyFieldEmpty){
                Toast.makeText(view.getContext(), "Preencha todos os campos !", Toast.LENGTH_SHORT).show();
                return;
            }
            MySystem.registerUser(isCommom ? new Common(name, email) :new Teacher(name, email), password,getActivity());
        });
        return view;
    }

    private Drawable getBackgroundOrDefaultOrOkOrError(boolean condition1, boolean condition2) {
        return condition1 ? getResources().getDrawable(R.drawable.background_editext_default)
                : condition2 ? getResources().getDrawable(R.drawable.background_editext_ok)
                : getResources().getDrawable(R.drawable.background_editext_error);
    }

    private Drawable getBackgroundOrDefaultOrOk(boolean condition1) {
        return condition1 ? getResources().getDrawable(R.drawable.background_editext_default)
                : getResources().getDrawable(R.drawable.background_editext_ok);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

