package com.example.medicare.modelo;

public class Paciente implements Comparable<Paciente> {
    private String Nombres;
    private String apellidos;
    private String cumpleaños;
    private String celular;
    private String email;
    private String nss; //numero de seguro social
    private String estado;

    public Paciente() {
    }

    public Paciente(String Nombres, String apellidos, String cumpleaños, String celular, String email, String nss, String estado) {
        this.Nombres = Nombres;
        this.apellidos = apellidos;
        this.cumpleaños = cumpleaños;
        this.celular = celular;
        this.email = email;
        this.nss = nss;
        this.estado = estado;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getapellidos() {
        return apellidos;
    }

    public void setapellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getcumpleaños() {
        return cumpleaños;
    }

    public void setcumpleaños(String cumpleaños) {
        this.cumpleaños = cumpleaños;
    }

    public String getcelular() {
        return celular;
    }

    public void setcelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getnss() {
        return nss;
    }

    public void setnss(String nss) {
        this.nss = nss;
    }

    public String getestado() {
        return estado;
    }

    public void setestado(String estado) {
        this.estado = estado;
    }

    public String getNombreC()
    {
        return Nombres+" "+apellidos;
    }

    @Override
    public int compareTo(Paciente o) {
        return this.getNombreC().compareTo(o.getNombreC());
    }
}
