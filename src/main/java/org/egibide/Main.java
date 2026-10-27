package org.egibide;

import org.egibide.idao.SociaDaoImpl;
import org.egibide.models.Juego;
import org.egibide.models.ModoJuego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.getInstance();

        List<Socia> socias = new SociaDaoImpl().getSocias();
        List<Juego> juegos = cargarJuegos(socias);
        List<Partida> partidas = cargarPartidas(juegos);

        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal(socias, partidas, juegos);
            JFrame frame = new JFrame("Menu principal");
            frame.setContentPane(menu.panelMenu);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static List<Juego> cargarJuegos(List<Socia> socias) throws SQLException {
        String query = "select id_juego, nombre, autor, descripcion, min_jugadores, max_jugadores, "
                + "duracion, tematica, modo_juego, localizacion, dueno, fecha_alta from juego";
        Map<Integer, Socia> sociasPorId = new HashMap<>();
        for (Socia socia : socias) {
            sociasPorId.put(socia.getId(), socia);
        }

        List<Juego> juegos = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int idDueno = resultSet.getInt("dueno");
                Socia dueno = resultSet.wasNull() ? null : sociasPorId.get(idDueno);
                String modo = resultSet.getString("modo_juego");
                Juego juego = new Juego(
                        resultSet.getInt("id_juego"),
                        resultSet.getString("nombre"),
                        resultSet.getString("autor"),
                        resultSet.getString("descripcion"),
                        resultSet.getInt("min_jugadores"),
                        resultSet.getInt("max_jugadores"),
                        resultSet.getInt("duracion"),
                        resultSet.getString("tematica"),
                        modo == null ? null : ModoJuego.valueOf(modo),
                        resultSet.getString("localizacion"),
                        dueno,
                        resultSet.getDate("fecha_alta")
                );
                juegos.add(juego);
            }
        }
        return juegos;
    }

    private static List<Partida> cargarPartidas(List<Juego> juegos) throws SQLException {
        String query = "select id_partida, fecha, hora, jugadores_deseados, id_juego_fk from partida";
        Map<Integer, Juego> juegosPorId = new HashMap<>();
        for (Juego juego : juegos) {
            juegosPorId.put(juego.getId(), juego);
        }

        List<Partida> partidas = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Partida partida = new Partida(
                        resultSet.getInt("id_partida"),
                        resultSet.getDate("fecha"),
                        resultSet.getInt("hora"),
                        resultSet.getInt("jugadores_deseados")
                );
                int idJuego = resultSet.getInt("id_juego_fk");
                if (!resultSet.wasNull()) {
                    Juego juego = juegosPorId.get(idJuego);
                    partida.setJuego(juego);
                    if (juego != null) {
                        juego.getPartidas().add(partida);
                    }
                }
                partidas.add(partida);
            }
        }
        return partidas;
    }
}
