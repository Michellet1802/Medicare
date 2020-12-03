package com.example.medicare.modelo;

public class Consulta {
    String doctorNombre;
    String doctorEmail;
    String pacienteEmail;
    String enfermedad;
    String fecha;
    String precio;
    String prescripcion;

    public Consulta() {
    }

    public Consulta(String doctorNombre, String doctorEmail, String pacienteEmail, String enfermedad, String fecha, String precio, String prescripcion) {
        this.doctorNombre = doctorNombre;
        this.doctorEmail = doctorEmail;
        this.pacienteEmail = pacienteEmail;
        this.enfermedad = enfermedad;
        this.fecha = fecha;
        this.precio = precio;
        this.prescripcion = prescripcion;
    }

    public String getdoctorNombre() {
        return doctorNombre;
    }

    public void setdoctorNombre(String doctorNombre) {
        this.doctorNombre = doctorNombre;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getpacienteEmail() {
        return pacienteEmail;
    }

    public void setpacienteEmail(String pacienteEmail) {
        this.pacienteEmail = pacienteEmail;
    }

    public String getenfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getfecha() {
        return fecha;
    }

    public void setTime(String fecha) {
        this.fecha = fecha;
    }

    public String getprecio() {
        return precio;
    }

    public void setprecio(String precio) {
        this.precio = precio;
    }

    public String getprescripcion() {
        return prescripcion;
    }

    public void setprescripcion(String prescripcion) {
        this.prescripcion = prescripcion;
    }
}
