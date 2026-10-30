package org.egibide.utils;

import org.egibide.models.Participante;
import org.egibide.models.Partida;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Modelo de tabla Swing que presenta partidas y resume participantes y ganadoras.
 */
public class ModTabla_GesPartidas extends AbstractTableModel {
    private final List<Partida> partidas;

    private final String[] titulos = {
            "Id", "Juego", "Fecha", "Hora", "Jugadores deseados", "Participantes", "Ganadoras"
    };

    /**
     * Crea el modelo usando la lista compartida que muestra la tabla.
     *
     * @param partidas lista de partidas que se presentará
     */
    public ModTabla_GesPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }


    /**
     * @return número de filas, una por partida
     */
    @Override
    public int getRowCount() {
        return partidas.size();
    }

    /**
     * @return número de columnas de la vista de partidas
     */
    @Override
    public int getColumnCount() {
        return titulos.length;
    }

    /**
     * Devuelve el dato o resumen que se presenta en una celda.
     *
     * @param rowIndex    índice de la fila
     * @param columnIndex índice de la columna
     * @return valor que se mostrará en la celda
     * @throws IndexOutOfBoundsException si el índice de columna no es válido
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Partida partida = partidas.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return partida.getId();
            case 1:
                return partida.getJuego() == null ? null : partida.getJuego().getNombre();
            case 2:
                return partida.getFecha();
            case 3:
                return partida.getHora();
            case 4:
                return partida.getJugadoresDeseados();
            case 5:
                if (partida.getJugadoras().isEmpty()) {
                    return "Sin jugadoras";
                }
                StringBuilder jugadoras = new StringBuilder();
                for (Participante participante : partida.getJugadoras()) {
                    if (jugadoras.length() > 0) {
                        jugadoras.append(", ");
                    }
                    if (participante.getSocia() == null) {
                        jugadoras.append("Participante ").append(participante.getId());
                    } else {
                        jugadoras.append(participante.getSocia().getNombre()).append(" ")
                                .append(participante.getSocia().getApellidos());
                    }
                }
                return jugadoras.toString();
            case 6:
                if (partida.getGanadoras().isEmpty()) {
                    return "Sin ganadoras";
                }
                StringBuilder ganadoras = new StringBuilder();
                for (Participante participante : partida.getGanadoras()) {
                    if (ganadoras.length() > 0) {
                        ganadoras.append(", ");
                    }
                    if (participante.getSocia() == null) {
                        ganadoras.append("Participante ").append(participante.getId());
                    } else {
                        ganadoras.append(participante.getSocia().getNombre()).append(" ")
                                .append(participante.getSocia().getApellidos());
                    }
                }
                return ganadoras.toString();
            default:
                throw new IndexOutOfBoundsException("Índice de columna no válido: " + columnIndex);
        }
    }

    /**
     * Devuelve el encabezado de la columna indicada.
     *
     * @param column índice de la columna
     * @return título de la columna
     */
    @Override
    public String getColumnName(int column) {
        return titulos[column];
    }

}
