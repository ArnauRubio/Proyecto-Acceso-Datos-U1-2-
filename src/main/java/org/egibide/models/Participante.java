package org.egibide.models;

/**
 * Vincula a una socia con una partida e indica si resultó ganadora.
 */
public class Participante {
    private int id;
    private boolean ganadora;

    private Partida partida;
    private Socia socia;

    /**
     * Crea un registro de participación.
     *
     * @param id       identificador de la participación
     * @param ganadora {@code true} si la participante ganó la partida
     */
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

    public boolean isGanadora() {
        return ganadora;
    }

    public void setGanadora(boolean ganadora) {
        this.ganadora = ganadora;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Socia getSocia() {
        return socia;
    }

    public void setSocia(Socia socia) {
        this.socia = socia;
    }

    @Override
    public String toString() {
        return "Participante{" +
                "id=" + id +
                ", ganadora=" + ganadora +
                ", partida=" + partida +
                ", socia=" + socia +
                '}';
    }
}
