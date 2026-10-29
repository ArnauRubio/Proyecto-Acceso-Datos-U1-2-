package org.egibide.utils;

import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseInserts {
    private DatabaseInserts() {
    }

    public static Asociacion findAssociation(String value) throws SQLException {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Indica el nombre o el ID de la asociación.");
        }
        boolean searchById;
        int associationId = 0;
        try {
            associationId = Integer.parseInt(normalized);
            searchById = true;
        } catch (NumberFormatException ex) {
            searchById = false;
        }
        String query = "SELECT id_asociacion, nombre, fecha_apertura, direccion, cif FROM asociacion WHERE "
                + (searchById ? "id_asociacion = ?" : "LOWER(nombre) = LOWER(?)");
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            if (searchById) {
                statement.setInt(1, associationId);
            } else {
                statement.setString(1, normalized);
            }
            try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("No se encontró la asociación indicada.");
                }
                Asociacion asociacion = new Asociacion(resultSet.getInt("id_asociacion"),
                        resultSet.getString("nombre"), resultSet.getDate("fecha_apertura"),
                        resultSet.getString("direccion"), resultSet.getString("cif"));
                if (!searchById && resultSet.next()) {
                    throw new IllegalArgumentException("Hay varias asociaciones con ese nombre; indica su ID.");
                }
                return asociacion;
            }
        }
    }

    public static int insertSocia(Socia socia, Asociacion asociacion) throws SQLException {
        String query = "INSERT INTO socia (id_socia, nombre, apellidos, edad, email, id_asociacion_fk) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, socia.getId());
            statement.setString(2, socia.getNombre());
            statement.setString(3, socia.getApellidos());
            statement.setInt(4, socia.getEdad());
            statement.setString(5, socia.getEmail());
            statement.setInt(6, asociacion.getId());
            return statement.executeUpdate();
        }
    }

    public static boolean updateSocia(Socia socia, Asociacion asociacion) throws SQLException {
        String query = "UPDATE socia SET nombre = ?, apellidos = ?, edad = ?, email = ?, id_asociacion_fk = ? "
                + "WHERE id_socia = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setString(1, socia.getNombre());
            statement.setString(2, socia.getApellidos());
            statement.setInt(3, socia.getEdad());
            statement.setString(4, socia.getEmail());
            statement.setInt(5, asociacion.getId());
            statement.setInt(6, socia.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public static int insertJuego(Juego juego) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String asociacionColumn = findAssociationColumn(connection, "juego");
        String query = "INSERT INTO juego (id_juego, nombre, autor, descripcion, min_jugadores, max_jugadores, "
                + "duracion, tematica, modo_juego, localizacion, dueno, fecha_alta"
                + (asociacionColumn == null ? "" : ", `" + asociacionColumn + "`")
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
                + (asociacionColumn == null ? "" : ", ?") + ")";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, juego.getId());
            statement.setString(2, juego.getNombre());
            statement.setString(3, juego.getAutor());
            statement.setString(4, juego.getDescripcion());
            statement.setInt(5, juego.getMinJugadores());
            statement.setInt(6, juego.getMaxJugadores());
            statement.setInt(7, juego.getDuracion());
            statement.setString(8, juego.getTematica());
            statement.setString(9, juego.getModoJuego().toString());
            statement.setString(10, juego.getLocalizacion());
            if (juego.getDueno() == null) {
                statement.setNull(11, Types.INTEGER);
            } else {
                statement.setInt(11, juego.getDueno().getId());
            }
            statement.setDate(12, juego.getFechaAlta());
            if (asociacionColumn != null) {
                statement.setInt(13, resolveGameAssociationId(juego));
            }
            return statement.executeUpdate();
        }
    }

    public static boolean updateJuego(Juego juego) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String asociacionColumn = findAssociationColumn(connection, "juego");
        String query = "UPDATE juego SET nombre = ?, autor = ?, descripcion = ?, min_jugadores = ?, "
                + "max_jugadores = ?, duracion = ?, tematica = ?, modo_juego = ?, localizacion = ?, "
                + "dueno = ?, fecha_alta = ?"
                + (asociacionColumn == null ? "" : ", `" + asociacionColumn + "` = ?")
                + " WHERE id_juego = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, juego.getNombre());
            statement.setString(2, juego.getAutor());
            statement.setString(3, juego.getDescripcion());
            statement.setInt(4, juego.getMinJugadores());
            statement.setInt(5, juego.getMaxJugadores());
            statement.setInt(6, juego.getDuracion());
            statement.setString(7, juego.getTematica());
            statement.setString(8, juego.getModoJuego().toString());
            statement.setString(9, juego.getLocalizacion());
            if (juego.getDueno() == null) {
                statement.setNull(10, Types.INTEGER);
            } else {
                statement.setInt(10, juego.getDueno().getId());
            }
            statement.setDate(11, juego.getFechaAlta());
            int idParameter = 12;
            if (asociacionColumn != null) {
                statement.setInt(idParameter++, resolveGameAssociationId(juego));
            }
            statement.setInt(idParameter, juego.getId());
            return statement.executeUpdate() > 0;
        }
    }

    private static String findAssociationColumn(Connection connection, String table) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        List<String> columnNames = new ArrayList<>();
        try (ResultSet columns = metadata.getColumns(connection.getCatalog(), null, table, null)) {
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        }
        for (String candidate : new String[]{"id_asociacion_fk", "id_asociacion"}) {
            for (String columnName : columnNames) {
                if (candidate.equalsIgnoreCase(columnName)) {
                    return columnName;
                }
            }
        }
        return null;
    }

    private static int resolveGameAssociationId(Juego juego) throws SQLException {
        if (juego.getDueno() != null && juego.getDueno().getAsociacion() != null) {
            return juego.getDueno().getAsociacion().getId();
        }
        String query = "SELECT id_asociacion FROM asociacion ORDER BY id_asociacion";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
             java.sql.ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                throw new SQLException("No hay ninguna asociación registrada para asignar el juego.");
            }
            int id = resultSet.getInt("id_asociacion");
            if (resultSet.next()) {
                throw new SQLException("Selecciona una propietaria asociada para determinar la asociación del juego.");
            }
            return id;
        }
    }
}
