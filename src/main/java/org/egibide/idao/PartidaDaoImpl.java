package org.egibide.idao;

import org.egibide.dao.PartidaDao;
import org.egibide.models.Juego;
import org.egibide.models.ModoJuego;
import org.egibide.models.Partida;
import org.egibide.models.Participante;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.Date;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa las consultas SQL de partidas y las escrituras transaccionales relacionadas.
 */
public class PartidaDaoImpl implements PartidaDao {

    /**
     * Comprueba si existe una partida con el identificador indicado.
     */
    public boolean partidaExists(int id) {
        String query = "select * from partida where id=?";
        PreparedStatement ps;
        boolean partidaExists = false;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                partidaExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidaExists;
    }

    @Override
    public Partida getPartida(int id) {
        String query = "select * from partida where id=?";
        PreparedStatement ps = null;
        Partida partida = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                partida = new Partida(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getInt("hora"),
                        rs.getInt("jugadoresDeseados")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partida;
    }

    @Override
    public List<Partida> getPartidas() {
        List<Partida> partidas = new ArrayList<>();

        String query = "select * from partida";
        PreparedStatement ps = null;
        Partida partida = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            //ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                partida = new Partida(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getInt("hora"),
                        rs.getInt("jugadoresDeseados")
                );
                partidas.add(partida);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidas;
    }

    @Override
    public int addPartida(Partida partida) {
        String query = "insert into partida values id=?, fecha=?, hora=?, jugadoresDeseados=?";
        PreparedStatement ps;
        int rs = 0;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

            ps.setInt(1, partida.getId());
            ps.setDate(2, partida.getFecha());
            ps.setInt(3, partida.getHora());
            ps.setInt(4, partida.getJugadoresDeseados());
            rs = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean update(Partida partida) {
        if (partidaExists(partida.getId())) {
            String query = "update socia set fecha=?, hora=?, jugadoresDeseados=? where id=?";
            PreparedStatement ps;
            int rs = 0;
            try {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

                ps.setDate(1, partida.getFecha());
                ps.setInt(2, partida.getHora());
                ps.setInt(3, partida.getJugadoresDeseados());
                ps.setInt(4, partida.getId());
                rs = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return (rs > 0);
        }
        return false;
    }

    @Override
    public void delete(int id) {
        String query = "delete from partida where id=?";
        PreparedStatement ps = null;

        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Partida> getPartidasByJuegoId(int id_juego) {
        List<Partida> partidas = new ArrayList<>();

        String query = "select * from partida where id_juego=?";
        PreparedStatement ps = null;
        Partida partida = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_juego);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                partida = new Partida(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getInt("hora"),
                        rs.getInt("jugadoresDeseados")
                );
                partidas.add(partida);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidas;
    }

    @Override
    public Partida getPartidaByParticipanteId(int id_participante) {
        String query = "select * from partida where id_partida=(select id_partida from participante where id_participante=?)";
        PreparedStatement ps = null;
        Partida partida = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_participante);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                partida = new Partida(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getInt("hora"),
                        rs.getInt("jugadoresDeseados")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partida;
    }

    /**
     * Guarda la partida y reemplaza sus participantes dentro de una transacción.
     */
    @Override
    public void savePartida(Partida partida, boolean nueva) throws SQLException {
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
                        + "ganadores = NULL, id_juego_fk = ? WHERE id_partida = ?";
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePartida(Partida partida) throws SQLException {
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

    /**
     * Sustituye las filas de participantes de la partida dentro de la transacción activa.
     */
    private void replaceParticipants(Connection connection, Partida partida) throws SQLException {
        String participantIdColumn = findColumn(connection, "participante", "id_participante", "id");
        String sociaColumn = findColumn(connection, "participante", "id_socia_fk", "id_socia");
        String partidaColumn = findColumn(connection, "participante", "id_partida_fk", "id_partida");
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM participante WHERE `" + partidaColumn + "` = ?")) {
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

    /**
     * Permite que la columna de ganadoras quede vacía mientras se actualizan participantes.
     */
    private void ensureGanadoresNullable(Connection connection) throws SQLException {
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

    /**
     * Comprueba si una columna se genera automáticamente en la base de datos.
     */
    private boolean isAutoIncrement(Connection connection, String table, String column) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet columns = metadata.getColumns(connection.getCatalog(), null, table, column)) {
            if (columns.next()) {
                return "YES".equalsIgnoreCase(columns.getString("IS_AUTOINCREMENT"));
            }
        }
        throw new SQLException("No se encontró la columna " + column + " en " + table + ".");
    }

    /**
     * Busca en la tabla la primera columna existente entre los nombres candidatos.
     */
    private String findColumn(Connection connection, String table, String... candidates) throws SQLException {
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
