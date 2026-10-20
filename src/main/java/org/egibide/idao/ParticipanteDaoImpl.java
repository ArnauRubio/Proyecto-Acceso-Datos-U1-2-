package org.egibide.idao;

import org.egibide.dao.ParticipanteDao;
import org.egibide.models.Participante;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteDaoImpl implements ParticipanteDao {

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
}
