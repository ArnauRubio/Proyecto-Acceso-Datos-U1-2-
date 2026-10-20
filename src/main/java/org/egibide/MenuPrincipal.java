package org.egibide;

import org.egibide.models.Socia;
import org.egibide.models.Partida;
import org.egibide.models.Juego;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuPrincipal {
    public JPanel panelMenu;
    private JButton buttonSocias;
    private JButton buttonJuegos;
    private JButton buttonPartidas;
    private JTextArea TAInfo;

    public MenuPrincipal(List<Socia> all_socias, List<Partida> all_partidas, List<Juego> all_juegos) {


        buttonSocias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("VGestionSocias");
                frame.setContentPane(new VGestionSocias(all_socias).panelSoc);
                frame.pack();
                frame.setVisible(true);
            }
        });
        buttonJuegos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Juegos");
                frame.setContentPane(new Juegos(all_juegos, all_socias).panelGestionJuegos);
                frame.pack();
                frame.setVisible(true);
            }
        });
        buttonPartidas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Partidas");
                frame.setContentPane(new Partidas(all_partidas, all_juegos, all_socias).panelGestionPartidas);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }

}