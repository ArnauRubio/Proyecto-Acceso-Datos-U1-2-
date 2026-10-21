package org.egibide;

import org.egibide.idao.JuegoDaoImpl;
import org.egibide.idao.PartidaDaoImpl;
import org.egibide.idao.SociaDaoImpl;
import org.egibide.models.Juego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.getInstance();

        List<Socia> socias = new SociaDaoImpl().getSocias();
        List<Partida> partidas = new PartidaDaoImpl().getPartidas();
        List<Juego> juegos = new JuegoDaoImpl().getJuegos();

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
}
