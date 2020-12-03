package com.example.medicare.modelo;

public class Relacion {
    String emailDoctor;
    String emailPaciente;

    public Relacion() {
    }

    public Relacion(String emailDoctor, String emailPaciente) {
        this.emailDoctor = emailDoctor;
        this.emailPaciente = emailPaciente;
    }

    public String getEmailDoctor() {
        return emailDoctor;
    }

    public void setEmailDoctor(String emailDoctor) {
        this.emailDoctor = emailDoctor;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }
}
