package org.egibide;

import org.egibide.models.Socia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuPrincipal {
    private JButton buttonSocias;
    private JButton buttonJuegos;
    private JButton buttonPartidas;
    private JTextArea TAInfo;

    public MenuPrincipal(List<Socia> all_socias, List<Partidas> all_partidas, List<Juegos> all_juegos) {


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
                frame.setContentPane(new Juegos(all_juegos).panelGestionJuegos);
                frame.pack();
                frame.setVisible(true);
            }
        });
        buttonPartidas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Partidas");
                frame.setContentPane(new Partidas(all_socias, all_partidas).panelGestionPartidas);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }

}