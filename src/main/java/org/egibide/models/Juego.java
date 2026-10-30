package org.egibide.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Datos de un juego de mesa y sus relaciones con propietaria, asociación y partidas.
 */
public class Juego {
    private int id;
    private String nombre;
    private String autor;
    private String descripcion;
    private int minJugadores;
    private int maxJugadores;
    private int duracion;
    private String tematica;
    private ModoJuego modoJuego;
    private String localizacion;
    private Socia dueno;
    private Date fechaAlta;

    private Asociacion asociacion;
    private List<Partida> partidas;

    /**
     * Crea un juego con todos sus datos; la lista de partidas empieza vacía.
     *
     * @param id           identificador del juego
     * @param nombre       nombre del juego
     * @param autor        autor o autora
     * @param descripcion  descripción del juego
     * @param minJugadores mínimo de participantes
     * @param maxJugadores máximo de participantes
     * @param duracion     duración estimada
     * @param tematica     temática del juego
     * @param modoJuego    modalidad cooperativa o competitiva
     * @param localizacion ubicación física
     * @param dueno        socia propietaria; puede ser null
     * @param fechaAlta    fecha de alta del juego
     */
    public Juego(int id, String nombre, String autor, String descripcion, int minJugadores, int maxJugadores, int duracion, String tematica, ModoJuego modoJuego, String localizacion, Socia dueno, Date fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.descripcion = descripcion;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracion = duracion;
        this.tematica = tematica;
        this.modoJuego = modoJuego;
        this.localizacion = localizacion;
        this.dueno = dueno;
        this.fechaAlta = fechaAlta;
        this.partidas = new ArrayList<>();
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(int minJugadores) {
        this.minJugadores = minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public ModoJuego getModoJuego() {
        return modoJuego;
    }

    public void setModoJuego(ModoJuego modoJuego) {
        this.modoJuego = modoJuego;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public Socia getDueno() {
        return dueno;
    }

    public void setDueno(Socia dueno) {
        this.dueno = dueno;
    }

    public java.sql.Date getFechaAlta() {
        return (java.sql.Date) fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    @Override
    public String toString() {
        return "Juego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", autor='" + autor + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", minJugadores=" + minJugadores +
                ", maxJugadores=" + maxJugadores +
                ", duracion=" + duracion +
                ", tematica='" + tematica + '\'' +
                ", modoJuego=" + modoJuego +
                ", localizacion='" + localizacion + '\'' +
                ", propietaria=" + dueno +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
}
