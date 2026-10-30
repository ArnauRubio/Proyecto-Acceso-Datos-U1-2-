package org.egibide.idao;

import org.egibide.dao.JuegoDao;
import org.egibide.models.Juego;
import org.egibide.models.ModoJuego;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Implementa las consultas y modificaciones SQL del catálogo de juegos.
 */
public class JuegoDaoImpl implements JuegoDao {

    /**
     * Comprueba si existe un juego con el identificador indicado.
     */
    public boolean juegoExists(int id) {
        String query = "select * from juego where id_juego=?";
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
        String query = "select * from juego where id_juego=?";
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
                        rs.getInt("id_juego"),
                        rs.getString("nombre"),
                        rs.getString("autor"),
                        rs.getString("descripcion"),
                        rs.getInt("min_jugadores"),
                        rs.getInt("max_jugadores"),
                        rs.getInt("duracion"),
                        rs.getString("tematica"),
                        ModoJuego.valueOf(rs.getString("modo_juego")),
                        rs.getString("localizacion"),
                        dueno,
                        rs.getDate("fecha_alta")
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
                        rs.getInt("min_jugadores"),
                        rs.getInt("max_jugadores"),
                        rs.getInt("duracion"),
                        rs.getString("tematica"),
                        ModoJuego.valueOf(rs.getString("modo_juego")),
                        rs.getString("localizacion"),
                        dueno,
                        rs.getDate("fecha_alta")
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
        String query = "insert into juego values id_juego=?, nombre=?, autor=?, descripcion=?, min_jugadores=?, max_jugadores=?, duracion=?, tematica=?, modo_juego=?, localizacion=?, propietaria=?, fecha_alta=?";
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
            String query = "update juego set nombre=?, autor=?, descripcion=?, min_jugadores=?, max_jugadores=?, duracion=?, tematica=?, modo_juego=?, localizacion=?, propietaria=?, fecha_alta=? where id_juego=?";
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
        String query = "delete from juego where id_juego=?";
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
                int idDueno = rs.getInt("dueno");
                Socia dueno = rs.wasNull()
                        ? null
                        : new SociaDaoImpl().getSocia(idDueno);

                juego = new Juego(
                        rs.getInt("id_juego"),
                        rs.getString("nombre"),
                        rs.getString("autor"),
                        rs.getString("descripcion"),
                        rs.getInt("min_jugadores"),
                        rs.getInt("max_jugadores"),
                        rs.getInt("duracion"),
                        rs.getString("tematica"),
                        ModoJuego.valueOf(rs.getString("modo_juego")),
                        rs.getString("localizacion"),
                        dueno,
                        rs.getDate("fecha_alta")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juego;
    }

    /**
     * Inserta un juego y resuelve su asociación si el esquema lo requiere.
     */
    @Override
    public int insertJuego(Juego juego) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String asociacionColumn = findAssociationColumn(connection, "juego");
        String query = "INSERT INTO juego (id_juego, nombre, autor, descripcion, min_jugadores, max_jugadores, "
                + "duracion, tematica, modo_juego, localizacion, dueno, fecha_alta"
                + (asociacionColumn == null ? "" : ", `" + asociacionColumn + "`")
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
                + (asociacionColumn == null ? "" : ", ?") + ")";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, juego.getId());
            statement.setString(2, juego.getNombre());
            statement.setString(3, juego.getAutor());
            statement.setString(4, juego.getDescripcion());
            statement.setInt(5, juego.getMinJugadores());
            statement.setInt(6, juego.getMaxJugadores());
            statement.setInt(7, juego.getDuracion());
            statement.setString(8, juego.getTematica());
            statement.setString(9, juego.getModoJuego().toString());
            statement.setString(10, juego.getLocalizacion());
            if (juego.getDueno() == null) {
                statement.setNull(11, Types.INTEGER);
            } else {
                statement.setInt(11, juego.getDueno().getId());
            }
            statement.setDate(12, juego.getFechaAlta());
            if (asociacionColumn != null) {
                statement.setInt(13, resolveGameAssociationId(juego));
            }
            return statement.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateJuego(Juego juego) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String asociacionColumn = findAssociationColumn(connection, "juego");
        String query = "UPDATE juego SET nombre = ?, autor = ?, descripcion = ?, min_jugadores = ?, "
                + "max_jugadores = ?, duracion = ?, tematica = ?, modo_juego = ?, localizacion = ?, "
                + "dueno = ?, fecha_alta = ?"
                + (asociacionColumn == null ? "" : ", `" + asociacionColumn + "` = ?")
                + " WHERE id_juego = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, juego.getNombre());
            statement.setString(2, juego.getAutor());
            statement.setString(3, juego.getDescripcion());
            statement.setInt(4, juego.getMinJugadores());
            statement.setInt(5, juego.getMaxJugadores());
            statement.setInt(6, juego.getDuracion());
            statement.setString(7, juego.getTematica());
            statement.setString(8, juego.getModoJuego().toString());
            statement.setString(9, juego.getLocalizacion());
            if (juego.getDueno() == null) {
                statement.setNull(10, Types.INTEGER);
            } else {
                statement.setInt(10, juego.getDueno().getId());
            }
            statement.setDate(11, juego.getFechaAlta());
            int idParameter = 12;
            if (asociacionColumn != null) {
                statement.setInt(idParameter++, resolveGameAssociationId(juego));
            }
            statement.setInt(idParameter, juego.getId());
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Localiza el nombre de columna de asociación compatible con el esquema actual.
     */
    private String findAssociationColumn(Connection connection, String table) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        List<String> columnNames = new ArrayList<>();
        try (ResultSet columns = metadata.getColumns(connection.getCatalog(), null, table, null)) {
            while (columns.next()) {
                columnNames.add(columns.getString("COLUMN_NAME"));
            }
        }
        for (String candidate : new String[]{"id_asociacion_fk", "id_asociacion"}) {
            for (String columnName : columnNames) {
                if (candidate.equalsIgnoreCase(columnName)) {
                    return columnName;
                }
            }
        }
        return null;
    }

    /**
     * Determina la asociación del juego a partir de su propietaria o de la única asociación disponible.
     *
     * @throws SQLException si no puede determinarse una asociación única
     */
    private int resolveGameAssociationId(Juego juego) throws SQLException {
        if (juego.getDueno() != null && juego.getDueno().getAsociacion() != null) {
            return juego.getDueno().getAsociacion().getId();
        }
        String query = "SELECT id_asociacion FROM asociacion ORDER BY id_asociacion";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                throw new SQLException("No hay ninguna asociación registrada para asignar el juego.");
            }
            int id = resultSet.getInt("id_asociacion");
            if (resultSet.next()) {
                throw new SQLException("Selecciona una propietaria asociada para determinar la asociación del juego.");
            }
            return id;
        }
    }


}
