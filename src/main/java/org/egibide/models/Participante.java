package org.egibide.models;

public class Participante {
    private int id;
    private int id_partida;
    private int id_socia;
    private boolean ganadora;

    public Participante(int id, boolean ganadora) {
        this.id = id;
        this.ganadora = ganadora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_partida() {
        return id_partida;
    }

    public void setId_partida(int id_partida) {
        this.id_partida = id_partida;
    }

    public int getId_socia() {
        return id_socia;
    }

    public void setId_socia(int id_socia) {
        this.id_socia = id_socia;
    }

    public boolean isGanadora() {
        return ganadora;
    }

    public void setGanadora(boolean ganadora) {
        this.ganadora = ganadora;
    }

    @Override
    public String toString() {
        return "Participante{" +
                "id=" + id +
                ", id_partida=" + id_partida +
                ", id_socia=" + id_socia +
                ", ganadora=" + ganadora +
                '}';
    }
}
