package org.egibide.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partida {
    private int id;
    private Date fecha;
    private int hora;
    private int jugadoresDeseados;
    private List<Socia> ganadoras;

    private Juego juego;
    private List<Socia> socias;

    public Partida(int id, Date fecha, int hora, int jugadoresDeseados, Juego juego) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadoresDeseados = jugadoresDeseados;
        this.ganadoras = new ArrayList<>();
        this.juego = juego;
        this.socias = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
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

    public List<Socia> getGanadoras() {
        return ganadoras;
    }

    public void setGanadoras(List<Socia> ganadoras) {
        this.ganadoras = ganadoras;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public List<Socia> getSocias() {
        return socias;
    }

    public void setSocias(List<Socia> socias) {
        this.socias = socias;
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
