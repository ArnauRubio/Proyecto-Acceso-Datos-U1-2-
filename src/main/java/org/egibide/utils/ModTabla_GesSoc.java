package org.egibide.utils;

import org.egibide.models.Socia;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Modelo de tabla Swing que presenta los datos de las socias.
 */
public class ModTabla_GesSoc extends AbstractTableModel {


    private List<Socia> socias;

    private String[] titulos = {"Id", "Nombre", "Apellidos", "edad", "Email"};

    /**
     * Crea el modelo usando la lista compartida que muestra la tabla.
     *
     * @param all_socias lista de socias que se presentará
     */
    public ModTabla_GesSoc(List<Socia> all_socias) {
        this.socias = all_socias;
    }


    /**
     * @return número de filas, una por socia
     */
    @Override
    public int getRowCount() {
        return socias.size();
    }

    /**
     * @return número de columnas definidas para la vista de socias
     */
    @Override
    public int getColumnCount() {
        return titulos.length;
    }

    /**
     * Devuelve el dato de la socia correspondiente a la celda solicitada.
     *
     * @param rowIndex    índice de la fila
     * @param columnIndex índice de la columna
     * @return valor que se mostrará en la celda
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return socias.get(rowIndex).getId();
            case 1:
                return socias.get(rowIndex).getNombre();
            case 2:
                return socias.get(rowIndex).getApellidos();
            case 3:
                return socias.get(rowIndex).getEdad();
            case 4:
                return socias.get(rowIndex).getEmail();
            case 5:
                return socias.get(rowIndex).getParticipaciones();
            default:
        }
        return null;
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
