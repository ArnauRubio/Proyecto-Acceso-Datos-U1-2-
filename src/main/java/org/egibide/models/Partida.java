package org.egibide.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partida {
    private int id;
    private Date fecha;
    private int hora;
    private int jugadoresDeseados;

    private List<Participante> ganadoras;
    private Juego juego;
    private List<Participante> jugadoras;

    public Partida(int id, Date fecha, int hora, int jugadoresDeseados) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadoresDeseados = jugadoresDeseados;
        this.ganadoras = new ArrayList<>();
        this.jugadoras = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.sql.Date getFecha() {
        return (java.sql.Date) fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getJugadoresDeseados() {
        return jugadoresDeseados;
    }

    public void setJugadoresDeseados(int jugadoresDeseados) {
        this.jugadoresDeseados = jugadoresDeseados;
    }

    public List<Participante> getGanadoras() {
        return ganadoras;
    }

    public void setGanadoras(List<Participante> ganadoras) {
        this.ganadoras = ganadoras;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public List<Participante> getJugadoras() {
        return jugadoras;
    }

    public void setJugadoras(List<Participante> jugadoras) {
        this.jugadoras = jugadoras;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", jugadoresDeseados=" + jugadoresDeseados +
                ", ganadoras=" + ganadoras +
                '}';
    }
}
