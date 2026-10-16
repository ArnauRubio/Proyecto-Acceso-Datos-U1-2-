package org.egibide;

import org.egibide.models.Socia;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModTabla_GesSoc extends AbstractTableModel {


    private List<Socia> socias;

    private String[] titulos = {"Id", "Nombre", "Apellidos", "edad", "Email"};

    /**
     * Muestra la información de todos los jugadores registrados.
     *
     * @param all_socias lista de todos los jugadores
     */
    public ModTabla_GesSoc(List<Socia> all_socias) {
        this.socias = all_socias;
    }


    @Override
    public int getRowCount() {
        return socias.size();
    }

    @Override
    public int getColumnCount() {
        return titulos.length;
    }

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
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return titulos[column];
    }
}
