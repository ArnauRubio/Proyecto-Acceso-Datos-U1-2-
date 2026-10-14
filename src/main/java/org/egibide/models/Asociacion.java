package org.egibide.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Asociacion {
    private int id;
    private String nombre;
    private Date fecha_apertura;
    private String direccion;
    private String cif;

    private List<Socia> socias;
    private List<Juego> juegos;

    public Asociacion() {
    }

    public Asociacion(int id, String nombre, Date fecha_apertura, String direccion, String cif) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_apertura = fecha_apertura;
        this.direccion = direccion;
        this.cif = cif;
        this.socias = new ArrayList<>();
        this.juegos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha_apertura() {
        return fecha_apertura;
    }

    public void setFecha_apertura(Date fecha_apertura) {
        this.fecha_apertura = fecha_apertura;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public List<Socia> getSocias() {
        return socias;
    }

    public void setSocias(List<Socia> socias) {
        this.socias = socias;
    }

    public List<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }

    @Override
    public String toString() {
        return "Asociacion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha_apertura=" + fecha_apertura +
                ", direccion='" + direccion + '\'' +
                ", cif='" + cif + '\'' +
                '}';
    }
}
