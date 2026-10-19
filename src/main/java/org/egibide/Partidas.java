package org.egibide;

import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class Partidas {
    JPanel panelPartidas;
    private JLabel nJornada;
    private JTable tablaPartidas;
    private JTable tablaSocias;
    JPanel panelGestionPartidas;

    /**
     * Muestra una JTable que llama a una vista (código del equipo, victorias, derrotas)
     * y todos los partidos de la última jornada jugada.
     *
     * @param all_socias
     * @param all_partidas
     */
    public Partidas(List<Socia> all_socias, List<Partidas> all_partidas) {


        ResultSet rset;
        try {
            Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();

            rset = stmt.executeQuery("SELECT * FROM SOCIAS");

            tablaSocias.setModel(buildTableModel(rset));

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //Mostrar resultados de ultima jornada jugada
        /*try {
            int ultimaJornada_id = GestionBD.ultimaJornadaJugada();
            Jornada ultimaJor = all_partidas.get(ultimaJornada_id - 1);

            tablaPartidas.setModel(new ModTabla_DueClasif(ultimaJor));
            nJornada.setText(String.valueOf(ultimaJor.getN_jornada()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No hay objetos Jornada creados");
        }*/

    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();


        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }


        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }
}
