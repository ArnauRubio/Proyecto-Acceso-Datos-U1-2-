package org.egibide.idao;

import org.egibide.dao.SociaDao;
import org.egibide.models.Asociacion;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementa el acceso SQL a socias y la carga de su asociación.
 */
public class SociaDaoImpl implements SociaDao {

    /**
     * Comprueba si existe una socia con el identificador indicado.
     */
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
    public List<Socia> getSocias() {
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
    public int addSocia(Socia socia) {
        String query = "insert into socia id_socia=?, nombre=?, apellidos=?, edad=?, email=?";

        PreparedStatement ps;
        int rs = 0;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

            ps.setInt(1, socia.getId());
            ps.setString(2, socia.getNombre());
            ps.setString(3, socia.getApellidos());
            ps.setInt(4, socia.getEdad());
            ps.setString(5, socia.getEmail());
            rs = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean update(Socia socia) {
        if (sociaExists(socia.getId())) {
            String query = "update socia set id=?, nombre=?, apellidos=?, edad=?, email=? where id=?";
            PreparedStatement ps;
            int rs = 0;
            try {
                ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);

                ps.setInt(1, socia.getId());
                ps.setString(2, socia.getNombre());
                ps.setString(3, socia.getApellidos());
                ps.setInt(4, socia.getEdad());
                ps.setString(5, socia.getEmail());
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
        String query = "delete from socia where id_socia=?";
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
    public Socia getSociaByParticipanteId(int id_participante) {
        String query = "select * from socia where id_socia=(select id_socia from participante where id_participante=?)";
        PreparedStatement ps = null;
        Socia socia = null;
        try {
            ps = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
            ps.setInt(1, id_participante);
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

    /**
     *  Inserta una socia y guarda la asociación indicada.
     */
    @Override
    public int insertSocia(Socia socia, Asociacion asociacion) throws SQLException {
        String query = "INSERT INTO socia (id_socia, nombre, apellidos, edad, email, id_asociacion_fk) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1, socia.getId());
            statement.setString(2, socia.getNombre());
            statement.setString(3, socia.getApellidos());
            statement.setInt(4, socia.getEdad());
            statement.setString(5, socia.getEmail());
            statement.setInt(6, asociacion.getId());
            return statement.executeUpdate();
        }
    }

    /**
     * Actualiza los datos y la asociación de una socia.
     */
    @Override
    public boolean updateSocia(Socia socia, Asociacion asociacion) throws SQLException {
        String query = "UPDATE socia SET nombre = ?, apellidos = ?, edad = ?, email = ?, id_asociacion_fk = ? "
                + "WHERE id_socia = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            statement.setString(1, socia.getNombre());
            statement.setString(2, socia.getApellidos());
            statement.setInt(3, socia.getEdad());
            statement.setString(4, socia.getEmail());
            statement.setInt(5, asociacion.getId());
            statement.setInt(6, socia.getId());
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Completa las asociaciones de las socias ya cargadas.
     */
    @Override
    public void loadSociaAssociations(List<Socia> socias) throws SQLException {
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
}
