package org.egibide.idao;

import org.egibide.dao.SociaDao;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SociaDaoImpl implements SociaDao {

    public boolean sociaExists(int id) {
        String query = "select * from socia where id=?";
        PreparedStatement ps;
        boolean sociaExists = false;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sociaExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sociaExists;
    }

    @Override
    public Socia getSocia(int id) {
        String query = "select * from socia where id=?";
        PreparedStatement ps = null;
        Socia socia = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                socia = new Socia(rs.getInt("id_socia"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("edad"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socia;
    }

    @Override
    public List<Socia> getSocias(int id) {
        List<Socia> socias = new ArrayList<>();

        String query = "select * from socia";
        PreparedStatement ps = null;
        Socia socia = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            //ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                socia = new Socia(rs.getInt("id_socia"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("edad"),
                        rs.getString("email")
                );
                socias.add(socia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socias;
    }

    @Override
    public int addSocia(Socia socia) throws SQLException {
        String query = "insert into socia (id, nombre, apellidos, edad, email) values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {

            ps.setInt(1, socia.getId());
            ps.setString(2, socia.getNombre());
            ps.setString(3, socia.getApellidos());
            ps.setInt(4, socia.getEdad());
            ps.setString(5, socia.getEmail());
            return ps.executeUpdate();
        }
    }

    @Override
    public boolean update(Socia socia, int idOriginal) throws SQLException {
        String query = "update socia set id=?, nombre=?, apellidos=?, edad=?, email=? where id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, socia.getId());
            ps.setString(2, socia.getNombre());
            ps.setString(3, socia.getApellidos());
            ps.setInt(4, socia.getEdad());
            ps.setString(5, socia.getEmail());
            ps.setInt(6, idOriginal);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "delete from socia where id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
