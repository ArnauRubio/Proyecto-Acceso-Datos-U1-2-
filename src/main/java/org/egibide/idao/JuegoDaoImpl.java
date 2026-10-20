package org.egibide.idao;

import org.egibide.dao.JuegoDao;
import org.egibide.models.Juego;
import org.egibide.models.coop_comp;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                        coop_comp.valueOf(rs.getString("modoJuego")),
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
                        coop_comp.valueOf(rs.getString("coop_comp")),
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
    public int addJuego(Juego juego) throws SQLException {
        String query = "insert into juego (id, nombre, autor, descripcion, minJugadores, maxJugadores, duracion, tematica, modoJuego, localizacion, dueno, fechaAlta) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {

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
            return ps.executeUpdate();
        }
    }

    @Override
    public boolean update(Juego juego, int idOriginal) throws SQLException {
        String query = "update juego set id=?, nombre=?, autor=?, descripcion=?, minJugadores=?, maxJugadores=?, duracion=?, tematica=?, modoJuego=?, localizacion=?, dueno=?, fechaAlta=? where id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {

            ps.setInt(1, juego.getId());
            ps.setString(2, juego.getNombre());
            ps.setString(3, juego.getAutor());
            ps.setString(4, juego.getDescripcion());
            ps.setInt(5, juego.getMinJugadores());
            ps.setInt(6, juego.getMaxJugadores());
            ps.setInt(7, juego.getDuracion());
            ps.setString(8, juego.getTematica());
            ps.setString(9, juego.getModoJuego().name());
            ps.setString(10, juego.getLocalizacion());

            if (juego.getDueno() == null) {
                ps.setNull(11, java.sql.Types.INTEGER);
            } else {
                ps.setInt(11, juego.getDueno().getId());
            }

            ps.setDate(12, juego.getFechaAlta());
            ps.setInt(13, idOriginal);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "delete from juego where id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
