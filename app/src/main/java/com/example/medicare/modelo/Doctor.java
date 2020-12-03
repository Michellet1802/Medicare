package com.example.medicare.modelo;

public class Doctor implements Comparable<Doctor> {
    private String nombreC;
    private String codigo;
    private String celular;
    private String email;
    private String ciudad;
    private String direccion;
    private String especialidad;

    public Doctor() {
    }

    public Doctor(String nombreC, String codigo, String celular, String email, String ciudad, String direccion, String especialidad) {
        this.nombreC = nombreC;
        this.codigo = codigo;
        this.celular = celular;
        this.email = email;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.especialidad = especialidad;
    }

    public String getnombreC() {
        return nombreC;
    }

    public void setnombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getcodigo() {
        return codigo;
    }

    public void setcodigo(String codigo) {
        this.codigo = codigo;
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

    public String getciudad() {
        return ciudad;
    }

    public void setciudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getdireccion() {
        return direccion;
    }

    public void setdireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getespecialidad() {
        return especialidad;
    }

    public void setespecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public int compareTo(Doctor o) {
        return this.getnombreC().compareTo(o.getnombreC());
    }
}
