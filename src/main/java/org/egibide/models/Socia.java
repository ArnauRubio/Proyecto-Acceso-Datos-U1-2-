package org.egibide.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Información de una socia, su asociación y sus participaciones en partidas.
 */
public class Socia {
    private int id;
    private String nombre;
    private String apellidos;
    private int edad;
    private String email;

    private Asociacion asociacion;
    private List<Participante> participaciones;

    /**
     * Crea una socia e inicializa su lista de participaciones.
     *
     * @param id        identificador de la socia
     * @param nombre    nombre
     * @param apellidos apellidos
     * @param edad      edad
     * @param email     correo electrónico
     */
    public Socia(int id, String nombre, String apellidos, int edad, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.email = email;
        this.participaciones = new ArrayList<>();
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public List<Participante> getParticipaciones() {
        return participaciones;
    }

    public void setParticipaciones(List<Participante> participaciones) {
        this.participaciones = participaciones;
    }

    @Override
    public String toString() {
        return "Socia{" +
                "email='" + email + '\'' +
                ", edad=" + edad +
                ", apellidos='" + apellidos + '\'' +
                ", nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }
}
