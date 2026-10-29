package org.egibide.utils;

import org.egibide.models.Asociacion;
import org.egibide.models.Participante;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DatabaseRelations {
    private DatabaseRelations() {
    }

    public static void loadSociaAssociations(List<Socia> socias) throws SQLException {
        String query = "SELECT s.id_socia, a.id_asociacion, a.nombre, a.fecha_apertura, a.direccion, a.cif "
                + "FROM socia s JOIN asociacion a ON a.id_asociacion = s.id_asociacion_fk";
        Map<Integer, Socia> sociasPorId = new HashMap<>();
        for (Socia socia : socias) {
            sociasPorId.put(socia.getId(), socia);
        }
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Socia socia = sociasPorId.get(resultSet.getInt("id_socia"));
                if (socia != null) {
                    Asociacion asociacion = new Asociacion(resultSet.getInt("id_asociacion"),
                            resultSet.getString("nombre"), resultSet.getDate("fecha_apertura"),
                            resultSet.getString("direccion"), resultSet.getString("cif"));
                    socia.setAsociacion(asociacion);
                    asociacion.getSocias().add(socia);
                }
            }
        }
    }

    public static void loadParticipants(List<Partida> partidas, List<Socia> socias) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String idColumn = findColumn(connection, "participante", "id_participante", "id");
        String sociaColumn = findColumn(connection, "participante", "id_socia_fk", "id_socia");
        String partidaColumn = findColumn(connection, "participante", "id_partida_fk", "id_partida");
        String query = "SELECT `" + idColumn + "` AS participant_id, ganadora, `" + sociaColumn
                + "` AS socia_id, `" + partidaColumn + "` AS partida_id FROM participante";
        Map<Integer, Socia> sociasPorId = new HashMap<>();
        Map<Integer, Partida> partidasPorId = new HashMap<>();
        for (Socia socia : socias) {
            sociasPorId.put(socia.getId(), socia);
        }
        for (Partida partida : partidas) {
            partidasPorId.put(partida.getId(), partida);
        }
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Participante participante = new Participante(resultSet.getInt("participant_id"),
                        resultSet.getBoolean("ganadora"));
                Socia socia = sociasPorId.get(resultSet.getInt("socia_id"));
                Partida partida = partidasPorId.get(resultSet.getInt("partida_id"));
                if (partida == null) {
                    throw new SQLException("Un participante referencia una partida que no se ha podido cargar.");
                }
                if (socia == null) {
                    throw new SQLException("Un participante referencia una socia que no se ha podido cargar.");
                }
                participante.setSocia(socia);
                participante.setPartida(partida);
                partida.getJugadoras().add(participante);
                if (participante.isGanadora()) {
                    partida.getGanadoras().add(participante);
                }
                socia.getParticipaciones().add(participante);
            }
        }
    }

    public static void savePartida(Partida partida, boolean nueva) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ensureGanadoresNullable(connection);
        boolean autoCommitAnterior = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            if (nueva) {
                String query = "INSERT INTO partida "
                        + "(id_partida, fecha, hora, jugadores, jugadores_deseados, ganadores, id_juego_fk) "
                        + "VALUES (?, ?, ?, ?, ?, NULL, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, partida.getId());
                    statement.setDate(2, partida.getFecha());
                    statement.setInt(3, partida.getHora());
                    statement.setInt(4, partida.getJugadoras().size());
                    statement.setInt(5, partida.getJugadoresDeseados());
                    statement.setInt(6, partida.getJuego().getId());
                    statement.executeUpdate();
                }
            } else {
                String query = "UPDATE partida SET fecha = ?, hora = ?, jugadores = ?, jugadores_deseados = ?, "
                        + "ganadores = NULL, id_juego_fk = ? "
                        + "WHERE id_partida = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setDate(1, partida.getFecha());
                    statement.setInt(2, partida.getHora());
                    statement.setInt(3, partida.getJugadoras().size());
                    statement.setInt(4, partida.getJugadoresDeseados());
                    statement.setInt(5, partida.getJuego().getId());
                    statement.setInt(6, partida.getId());
                    if (statement.executeUpdate() == 0) {
                        throw new SQLException("No se encontró la partida que se quiere actualizar.");
                    }
                }
            }
            replaceParticipants(connection, partida);
            connection.commit();
        } catch (SQLException | RuntimeException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommitAnterior);
        }
    }

    public static void deletePartida(Partida partida) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String partidaColumn = findColumn(connection, "participante", "id_partida_fk", "id_partida");
        boolean autoCommitAnterior = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM participante WHERE `" + partidaColumn + "` = ?")) {
                statement.setInt(1, partida.getId());
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM partida WHERE id_partida = ?")) {
                statement.setInt(1, partida.getId());
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException | RuntimeException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(autoCommitAnterior);
        }
    }

    private static void replaceParticipants(Connection connection, Partida partida) throws SQLException {
        String participantIdColumn = findColumn(connection, "participante", "id_participante", "id");
        String sociaColumn = findColumn(connection, "participante", "id_socia_fk", "id_socia");
        String partidaColumn = findColumn(connection, "participante", "id_partida_fk", "id_partida");
        String deleteQuery = "DELETE FROM participante WHERE `" + partidaColumn + "` = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, partida.getId());
            statement.executeUpdate();
        }

        boolean idAutogenerado = isAutoIncrement(connection, "participante", participantIdColumn);
        String insertQuery = "INSERT INTO participante (`" + participantIdColumn + "`, ganadora, `" + sociaColumn
                + "`, `" + partidaColumn + "`) VALUES (?, ?, ?, ?)";
        for (Participante participante : partida.getJugadoras()) {
            if (idAutogenerado) {
                insertQuery = "INSERT INTO participante (ganadora, `" + sociaColumn + "`, `" + partidaColumn
                        + "`) VALUES (?, ?, ?)";
            }
            try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                int parameter = 1;
                if (!idAutogenerado) {
                    statement.setInt(parameter++, participante.getId());
                }
                statement.setBoolean(parameter++, participante.isGanadora());
                statement.setInt(parameter++, participante.getSocia().getId());
                statement.setInt(parameter, partida.getId());
                statement.executeUpdate();
                if (idAutogenerado) {
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (keys.next()) {
                            participante.setId(keys.getInt(1));
                        }
                    }
                }
            }
        }
    }

    private static void ensureGanadoresNullable(Connection connection) throws SQLException {
        String type;
        boolean nullable;
        try (Statement statement = connection.createStatement();
             ResultSet columns = statement.executeQuery("SHOW COLUMNS FROM partida LIKE 'ganadores'")) {
            if (!columns.next()) {
                throw new SQLException("No se encontró la columna ganadores en la tabla partida.");
            }
            type = columns.getString("Type");
            nullable = "YES".equalsIgnoreCase(columns.getString("Null"));
        }
        if (!nullable) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("ALTER TABLE partida MODIFY COLUMN `ganadores` " + type + " NULL DEFAULT NULL");
            }
        }
    }

    private static boolean isAutoIncrement(Connection connection, String table, String column) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet columns = metadata.getColumns(connection.getCatalog(), null, table, column)) {
            if (columns.next()) {
                return "YES".equalsIgnoreCase(columns.getString("IS_AUTOINCREMENT"));
            }
        }
        throw new SQLException("No se encontró la columna " + column + " en " + table + ".");
    }

    private static String findColumn(Connection connection, String table, String... candidates) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        List<String> columnNames = new ArrayList<>();
        try (ResultSet columns = metadata.getColumns(connection.getCatalog(), null, table, null)) {
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        }
        for (String candidate : candidates) {
            for (String actualName : columnNames) {
                if (candidate.equalsIgnoreCase(actualName)) {
                    return actualName;
                }
            }
        }
        throw new SQLException("No se encontró una columna compatible para " + table + ".");
    }
}
