package org.egibide.utils;

import org.egibide.models.Partida;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModTabla_GesPartidas extends AbstractTableModel {
    private final List<Partida> partidas;

    private final String[] titulos = {
            "Id", "Juego", "Fecha", "Hora", "Jugadores deseados", "Jugadoras", "Ganadoras"
    };

    /**
     * Muestra la información de las partidas registradas.
     *
     * @param partidas lista de partidas
     */
    public ModTabla_GesPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }


    @Override
    public int getRowCount() {
        return partidas.size();
    }

    @Override
    public int getColumnCount() {
        return titulos.length;
    }

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
                return partida.getSocias();
            case 6:
                return partida.getGanadoras();
            default:
                throw new IndexOutOfBoundsException("Índice de columna no válido: " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return titulos[column];
    }

}
