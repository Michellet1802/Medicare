package com.example.medicare.modelo;

public class Cita {
    String fecha;
    String hora;
    String emailDoctor;
    String emailPaciente;
    String estado;
    static String[] estadoValues = {"En espera", "Aceptado", "Declinado"};

    public Cita() {

    }

    public Cita(String fecha, String hora, String emailDoctor, String emailPaciente) {
        this.fecha = fecha;
        this.hora = hora;
        this.emailDoctor = emailDoctor;
        this.emailPaciente = emailPaciente;
        this.estado = estadoValues[0];
    }

    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public String gethora() {
        return hora;
    }

    public void sethora(String hora) {
        this.hora = hora;
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

    public String getestado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setEstadoAceptado() {
        this.estado = estadoValues[1];
    }

    public void setEstadoDeclinado() {
        this.estado = estadoValues[2];
    }
}
