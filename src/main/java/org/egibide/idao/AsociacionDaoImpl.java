package org.egibide.idao;

import org.egibide.dao.AsociacionDao;
import org.egibide.models.Asociacion;
import org.egibide.utils.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementa las consultas y modificaciones de asociaciones sobre MariaDB.
 */
public class AsociacionDaoImpl implements AsociacionDao {

    /**
     * Comprueba si existe una asociación con el identificador indicado.
     */
    public boolean asociacionExists(int id) {
        String query = "select * from asociacion where id_asociacion=?";
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
        String query = "select * from asociacion where id_asociacion=?";
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
                        rs.getString("cif")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asociacion;
    }

    /**
     * Busca una asociación por su ID o por su nombre sin distinguir mayúsculas.
     */
    @Override
    public Asociacion findAssociation(String value) throws SQLException {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Indica el nombre o el ID de la asociación.");
        }
        int associationId = 0;
        boolean searchById;
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
            try (ResultSet resultSet = statement.executeQuery()) {
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

    @Override
    public boolean update(Asociacion asociacion) {
        if (asociacionExists(asociacion.getId())) {
            String query = "update asociacion set nombre=?, fecha_apertura=?, direccion=?, cif=? where id_asociacion=?";
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
        String query = "delete from asociacion where id_asociacion=?";
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
    public Asociacion getAsociacionByJuegoId(int id_juego) {
        String query = "select * from asociacion where id_asociacion=(select id_asociacion from juego where id_juego=?)";
        PreparedStatement ps = null;
        Asociacion asociacion = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_juego);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asociacion = new Asociacion(
                        rs.getInt("id_asociacion"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_apertura"),
                        rs.getString("direccion"),
                        rs.getString("cif")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asociacion;
    }

    @Override
    public Asociacion getAsociacionBySociaId(int id_socia) {
        String query = "select * from asociacion where id_asociacion=(select id_asociacion from socia where id_socia=?)";
        PreparedStatement ps = null;
        Asociacion asociacion = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_socia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                asociacion = new Asociacion(
                        rs.getInt("id_asociacion"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_apertura"),
                        rs.getString("direccion"),
                        rs.getString("cif")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asociacion;
    }
}
