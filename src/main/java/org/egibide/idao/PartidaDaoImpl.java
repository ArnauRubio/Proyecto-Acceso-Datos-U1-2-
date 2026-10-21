package org.egibide.idao;

import org.egibide.dao.PartidaDao;
import org.egibide.models.Juego;
import org.egibide.models.ModoJuego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartidaDaoImpl implements PartidaDao {

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
}
