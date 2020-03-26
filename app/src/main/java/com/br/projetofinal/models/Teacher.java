package com.br.projetofinal.models;

import java.text.DateFormat;

public class Teacher extends AbstractUser {
    public static final String TAG = "professor";

    public Teacher() {
    }

    public Teacher(String name, String email) {
        super(name, email);
        setTypeUser(Teacher.TAG);
    }

    @Override
    public String toString() {
        return "...\nTeacher{" +
                "\n     name:" +this.getName()+
                "\n     email:" +this.getEmail()+
                "\n     typeUser:"+this.getTypeUser()+
                "\n     timestamp:"+ DateFormat.getDateInstance(DateFormat.SHORT).format(this.getCreatedAt().toDate()) +
                "\n}";
    }
}
