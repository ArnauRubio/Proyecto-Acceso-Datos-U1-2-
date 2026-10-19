package org.egibide.idao;

import org.egibide.dao.AsociacionDao;
import org.egibide.models.Asociacion;
import org.egibide.utils.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsociacionDaoImpl implements AsociacionDao {

    public boolean asociacionExists(int id) {
        String query = "select * from asociacion where id=?";
        PreparedStatement ps;
        boolean asociacionExists = false;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asociacionExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asociacionExists;
    }

    @Override
    public Asociacion getAsociacion(int id) {
        String query = "select * from asociacion where id=?";
        PreparedStatement ps = null;
        Asociacion asociacion = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asociacion = new Asociacion(
                        rs.getInt("id_asociacion"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_apertura"),
                        rs.getString("direccion"),
                        rs.getString("CIF")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asociacion;
    }

    @Override
    public boolean update(Asociacion asociacion) {
        if (asociacionExists(asociacion.getId())) {
            String query = "update asociacion set nombre=?, fecha_apertura=?, direccion=?, cif=? where id=?";
            PreparedStatement ps;
            int rs = 0;
            try {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

                ps.setString(1, asociacion.getNombre());
                ps.setDate(2, (Date) asociacion.getFecha_apertura());
                ps.setString(3, asociacion.getDireccion());
                ps.setString(4, asociacion.getCif());
                ps.setInt(5, asociacion.getId());
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
        String query = "delete from asociacion where id=?";
        PreparedStatement ps = null;

        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
