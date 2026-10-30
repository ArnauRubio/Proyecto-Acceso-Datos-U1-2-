package org.egibide.idao;

import org.egibide.dao.ParticipanteDao;
import org.egibide.models.Partida;
import org.egibide.models.Participante;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementa las operaciones SQL de participación y enlaza las relaciones del modelo.
 */
public class ParticipanteDaoImpl implements ParticipanteDao {

    /**
     * Comprueba si existe una participación con el identificador indicado.
     */
    public boolean participanteExists(int id) {
        String query = "select * from participante where id=?";
        PreparedStatement ps;
        boolean participanteExists = false;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participanteExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participanteExists;
    }

    @Override
    public Participante getParticipante(int id) {
        String query = "select * from participante where id=?";
        PreparedStatement ps = null;
        Participante participante = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participante = new Participante(
                        rs.getInt("id"),
                        rs.getBoolean("ganadora")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participante;
    }

    @Override
    public List<Participante> getParticipantes() {
        List<Participante> participantes = new ArrayList<>();

        String query = "select * from participante";
        PreparedStatement ps = null;
        Participante participante = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            //ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participante = new Participante(
                        rs.getInt("id"),
                        rs.getBoolean("ganadora")
                );
                participantes.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participantes;
    }

    @Override
    public int addParticipante(Participante participante) {
        String query = "insert into participante values id=?, ganadora=?";
        PreparedStatement ps;
        int rs = 0;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

            ps.setInt(1, participante.getId());
            ps.setBoolean(2, participante.isGanadora());
            rs = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean update(Participante participante) {
        if (participanteExists(participante.getId())) {
            String query = "update participante set ganadora=? where id=?";
            PreparedStatement ps;
            int rs = 0;
            try {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

                ps.setInt(1, participante.getId());
                ps.setBoolean(2, participante.isGanadora());
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
        String query = "delete from participante where id=?";
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
    public List<Participante> getParticipantesByPartidaId(int id_partida) {
        List<Participante> participantes = new ArrayList<>();

        String query = "select * from participante where id_partida=?";
        PreparedStatement ps = null;
        Participante participante = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_partida);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participante = new Participante(
                        rs.getInt("id"),
                        rs.getBoolean("ganadora")
                );
                participantes.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participantes;
    }

    @Override
    public List<Participante> getGanadorasByPartidaId(int id_partida) {
        List<Participante> ganadoras = new ArrayList<>();

        String query = "select * from participante where id_partida=? and ganadora=?";
        PreparedStatement ps = null;
        Participante participante = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_partida);
            ps.setBoolean(2, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participante = new Participante(
                        rs.getInt("id"),
                        rs.getBoolean("ganadora")
                );
                ganadoras.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ganadoras;
    }

    @Override
    public List<Participante> getParticipacionesBySociaId(int id_socia) {
        List<Participante> participantes = new ArrayList<>();

        String query = "select * from participante where id_socia=?";
        PreparedStatement ps = null;
        Participante participante = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_socia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participante = new Participante(
                        rs.getInt("id"),
                        rs.getBoolean("ganadora")
                );
                participantes.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participantes;
    }

    /**
     * Enlaza las participaciones cargadas con sus partidas y socias correspondientes.
     */
    @Override
    public void loadParticipants(List<Partida> partidas, List<Socia> socias) throws SQLException {
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

    /**
     * Busca en la tabla la primera columna existente entre los nombres candidatos.
     */
    private String findColumn(Connection connection, String table, String... candidates) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        try (ResultSet columns = connection.getMetaData().getColumns(connection.getCatalog(), null, table, null)) {
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
