package com.br.projetofinal.models;

import java.text.DateFormat;

public class Common extends AbstractUser {
    public static final String TAG="comum";

    public Common() {}

    public Common(String name, String email) {
        super(name, email);
        setTypeUser(Common.TAG);
    }

    @Override
    public String toString() {
        return "...\nCommon{" +
                "\n     name:" +this.getName()+
                "\n     email:" +this.getEmail()+
                "\n     typeUser:"+this.getTypeUser()+
                "\n     timestamp:"+ DateFormat.getDateInstance(DateFormat.SHORT).format(this.getCreatedAt().toDate()) +
                "\n}";
    }
}
