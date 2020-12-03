package com.example.medicare.modelo;

public class Hospitalizacion implements Comparable<Hospitalizacion> {
    String nombreHospital;
    String emailPaciente;
    String fecha;
    String enfermedad;
    String precio;

    public Hospitalizacion() {
    }

    public Hospitalizacion(String nombreHospital, String emailPaciente, String fecha, String enfermedad, String precio) {
        this.nombreHospital = nombreHospital;
        this.emailPaciente = emailPaciente;
        this.fecha = fecha;
        this.enfermedad = enfermedad;
        this.precio = precio;
    }

    public String getnombreHospital() {
        return nombreHospital;
    }

    public void setnombreHospital(String nombreHospital) {
        this.nombreHospital = nombreHospital;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public String getenfermedad() {
        return enfermedad;
    }

    public void setenfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getprecio() {
        return precio;
    }

    public void setprecio(String precio) {
        this.precio = precio;
    }

    @Override
    public int compareTo(Hospitalizacion o) {
        String fecha2 = o.getfecha();
        String[] fechaSplitted1 = fecha.split("/");
        String[] fechaSplitted2 = fecha2.split("/");
        if(fechaSplitted1[0] != fechaSplitted2[0])
        {
            return Integer.parseInt(fechaSplitted1[0]) - Integer.parseInt(fechaSplitted1[0]);
        }
        else if(fechaSplitted1[1] != fechaSplitted2[1])
        {
            return Integer.parseInt(fechaSplitted1[1]) - Integer.parseInt(fechaSplitted1[1]);
        }
        else if(fechaSplitted1[2] != fechaSplitted2[2])
        {
            return Integer.parseInt(fechaSplitted1[2]) - Integer.parseInt(fechaSplitted1[2]);
        }
        else
            return 0;
    }
}
