package org.egibide.utils;

import org.egibide.models.Juego;
import org.egibide.models.Socia;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Modelo de tabla Swing que expone los datos visibles del catálogo de juegos.
 */
public class ModTabla_GesJuegos extends AbstractTableModel {

    private final List<Juego> juegos;

    private final String[] titulos = {"Id", "Nombre", "Duracion", "MinJugadores", "MaxJugadores", "Partidas", "Localizacion", "Tematica", "Dueña", "FechaAlta"};

    /**
     * Crea el modelo usando la lista compartida que muestra la tabla.
     *
     * @param all_juegos lista de juegos que se presentará
     */
    public ModTabla_GesJuegos(List<Juego> all_juegos) {
        this.juegos = all_juegos;
    }


    /**
     * @return número de filas, una por juego
     */
    @Override
    public int getRowCount() {
        return juegos.size();
    }

    /**
     * @return número de columnas definidas para la vista de juegos
     */
    @Override
    public int getColumnCount() {
        return titulos.length;
    }

    /**
     * Devuelve el valor mostrado en la celda indicada.
     *
     * @param rowIndex    índice de la fila
     * @param columnIndex índice de la columna
     * @return valor que se mostrará en la celda
     * @throws IndexOutOfBoundsException si el índice de columna no es válido
     */
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
                Socia propietaria = juego.getDueno();
                return propietaria == null ? "" : propietaria.getNombre() + " " + propietaria.getApellidos();
            case 9:
                return juego.getFechaAlta();
            default:
                throw new IndexOutOfBoundsException("Índice de columna no válido: " + columnIndex);
        }
    }

    /**
     * Devuelve el encabezado asociado a una columna.
     *
     * @param column índice de la columna
     * @return título de la columna
     */
    @Override
    public String getColumnName(int column) {
        return titulos[column];
    }
}
