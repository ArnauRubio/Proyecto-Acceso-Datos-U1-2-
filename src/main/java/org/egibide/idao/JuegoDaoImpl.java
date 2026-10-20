package org.egibide.idao;

import org.egibide.dao.JuegoDao;
import org.egibide.models.Juego;
import org.egibide.models.ModoJuego;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class JuegoDaoImpl implements JuegoDao {

    public boolean juegoExists(int id) {
        String query = "select * from juego where id=?";
        PreparedStatement ps;
        boolean juegoExists = false;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                juegoExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juegoExists;
    }

    @Override
    public Juego getJuego(int id) {
        String query = "select * from juego where id=?";
        PreparedStatement ps = null;
        Juego juego = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idDueno = rs.getInt("dueno");
                Socia dueno = rs.wasNull()
                        ? null
                        : new SociaDaoImpl().getSocia(idDueno);

                juego = new Juego(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("autor"),
                        rs.getString("descripcion"),
                        rs.getInt("minJugadores"),
                        rs.getInt("maxJugadores"),
                        rs.getInt("duracion"),
                        rs.getString("tematica"),
                        ModoJuego.valueOf(rs.getString("modoJuego")),
                        rs.getString("localizacion"),
                        dueno,
                        rs.getDate("fechaAlta")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juego;
    }

    @Override
    public List<Juego> getJuegos() {
        List<Juego> juegos = new ArrayList<>();

        String query = "select * from juego";
        PreparedStatement ps = null;
        Juego juego = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            //ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idDueno = rs.getInt("dueno");
                Socia dueno = rs.wasNull()
                        ? null
                        : new SociaDaoImpl().getSocia(idDueno);

                juego = new Juego(
                        rs.getInt("id_juego"),
                        rs.getString("nombre"),
                        rs.getString("autor"),
                        rs.getString("descripcion"),
                        rs.getInt("min_Jugadores"),
                        rs.getInt("max_Jugadores"),
                        rs.getInt("duracion"),
                        rs.getString("tematica"),
                        ModoJuego.valueOf(rs.getString("modoJuego")),
                        rs.getString("localizacion"),
                        dueno,
                        rs.getDate("fechaAlta")
                );
                juegos.add(juego);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juegos;
    }

    @Override
    public int addJuego(Juego juego) {
        String query = "insert into juego values id=?, nombre=?, autor=?, descripcion=?, minJugadores=?, maxJugadores=?, duracion=?, tematica=?, modoJuego=?, localizacion=?, propietaria=?, fechaAlta=?";
        PreparedStatement ps;
        int rs = 0;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

            ps.setInt(1, juego.getId());
            ps.setString(2, juego.getNombre());
            ps.setString(3, juego.getAutor());
            ps.setString(4, juego.getDescripcion());
            ps.setInt(5, juego.getMinJugadores());
            ps.setInt(6, juego.getMaxJugadores());
            ps.setInt(7, juego.getDuracion());
            ps.setString(8, juego.getTematica());
            ps.setString(9, juego.getModoJuego().toString());
            ps.setString(10, juego.getLocalizacion());

            if (juego.getDueno() == null) {
                ps.setNull(11, java.sql.Types.INTEGER);
            } else {
                ps.setInt(11, juego.getDueno().getId());
            }

            ps.setDate(12, juego.getFechaAlta());
            rs = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean update(Juego juego) {
        if (juegoExists(juego.getId())) {
            String query = "update juego set nombre=?, autor=?, descripcion=?, minJugadores=?, maxJugadores=?, duracion=?, tematica=?, modoJuego=?, localizacion=?, propietaria=?, fechaAlta=? where id=?";
            PreparedStatement ps;
            int rs = 0;
            try {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

                ps.setInt(1, juego.getId());
                ps.setString(2, juego.getNombre());
                ps.setString(3, juego.getAutor());
                ps.setString(4, juego.getDescripcion());
                ps.setInt(5, juego.getMinJugadores());
                ps.setInt(6, juego.getMaxJugadores());
                ps.setInt(7, juego.getDuracion());
                ps.setString(8, juego.getTematica());
                ps.setString(9, juego.getModoJuego().toString());
                ps.setString(10, juego.getLocalizacion());

            if (juego.getDueno() == null) {
                ps.setNull(11, java.sql.Types.INTEGER);
            } else {
                ps.setInt(11, juego.getDueno().getId());
            }

                ps.setDate(12, juego.getFechaAlta());
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
        String query = "delete from juego where id=?";
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
    public Juego getJuegoByPartidaId(int id_partida) {
        String query = "select * from juego where id_juego=(select id_juego from partida where id_partida=?)";
        PreparedStatement ps = null;
        Juego juego = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_partida);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idPropietaria = rs.getInt("propietaria");
                Socia propietaria = rs.wasNull()
                        ? null
                        : new SociaDaoImpl().getSocia(idPropietaria);

                juego = new Juego(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("autor"),
                        rs.getString("descripcion"),
                        rs.getInt("minJugadores"),
                        rs.getInt("maxJugadores"),
                        rs.getInt("duracion"),
                        rs.getString("tematica"),
                        ModoJuego.valueOf(rs.getString("modoJuego")),
                        rs.getString("localizacion"),
                        propietaria,
                        rs.getDate("fechaAlta")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juego;
    }


}
