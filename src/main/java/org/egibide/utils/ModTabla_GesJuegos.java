package org.egibide.utils;

import org.egibide.models.Juego;
import org.egibide.models.Socia;

import javax.swing.table.AbstractTableModel;

import java.util.List;

public class ModTabla_GesJuegos extends AbstractTableModel {

    private final List<Juego> juegos;

    private final String[] titulos = {"Id", "Nombre", "Duracion", "MinJugadores", "MaxJugadores", "Partidas", "Localizacion", "Tematica", "Dueña", "Autor", "FechaAlta"};

    /**
     * Muestra la información de todos los juegos registrados.
     *
     * @param all_juegos lista de todos los juegos
     */
    public ModTabla_GesJuegos(List<Juego> all_juegos) {
        this.juegos = all_juegos;
    }


    @Override
    public int getRowCount() {
        return juegos.size();
    }

    @Override
    public int getColumnCount() {
        return titulos.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Juego juego = juegos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return juego.getId();
            case 1:
                return juego.getNombre();
            case 2:
                return juego.getDuracion();
            case 3:
                return juego.getMinJugadores();
            case 4:
                return juego.getMaxJugadores();
            case 5:
                return juego.getPartidas().size();
            case 6:
                return juego.getLocalizacion();
            case 7:
                return juego.getTematica();
            case 8:
                Socia propietaria = juego.getPropietaria();
                return propietaria == null ? "" : propietaria.getNombre() + " " + propietaria.getApellidos();
            case 9:
                return juego.getAutor();
            case 10:
                return juego.getFechaAlta();
            default:
                throw new IndexOutOfBoundsException("Índice de columna no válido: " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return titulos[column];
    }
}
