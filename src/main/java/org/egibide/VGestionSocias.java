package org.egibide;

import org.egibide.models.Socia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VGestionSocias {
    private JPanel panelGestionJug;
    private JTable tableGestionSoc;
    private JButton borrarButton;
    private JButton editarButton;
    private JButton nuevoButton;
    private JTextField txtBuscar;
    private JButton buscarButton;
    private JComboBox comboBoxSocias;
    private JTextField txtID;
    private JTextField txtApellido;
    private JTextField txtNombre;
    private JButton buttonMostrar;
    private JTextPane textPane1;

    Socia soc = null;

    /**
     * Esta ventana permite al Administrador leer, crear, editar y borrar a los Jugadores.
     *
     * @param all_socias, para mostrarlos en el JList.
     */
    public VGestionSocias(List<Socia> all_socias) {

        tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

        nuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                int id = Integer.parseInt(txtID.getText());
                //Double salario = Double.parseDouble(txtSalario.getText());

                int id_soc = GestionBD.guardarJugador(id, nombre, apellido);
                Socia nuevoSoc = new Socia(id_soc, nombre, apellido);
                if (!all_socias.contains(nuevoSoc)) {
                    all_socias.add(nuevoSoc);
                }
                JOptionPane.showMessageDialog(null, "Jugador creado correctamente", "Nuevo jugador", JOptionPane.INFORMATION_MESSAGE);

                txtNombre.setText("");
                txtApellido.setText("");
                txtID.setText("");

                tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

                soc = null;
            }
        });

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //jug se instancia como retorno de buscar();
                if (soc == null) {
                    JOptionPane.showMessageDialog(null, "Busca o muestra (>) una socia", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    soc.setNombre(txtNombre.getText());
                    soc.setApellidos(txtApellido.getText());
                    soc.setId(Integer.parseInt(txtID.getText()));

                    txtNombre.setText("");
                    txtApellido.setText("");
                    txtID.setText("");

                    GestionBD.actualizarSocia(soc);

                    tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

                    soc = null;
                }
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //jug se instancia como retorno de buscar();
                if (soc == null) {
                    JOptionPane.showMessageDialog(null, "Busca o muestra (>) un jugador", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        //Si jug.getEquipo() es null entra en catch
                        soc.getAsociacion().getSocias().remove(soc);
                        soc.setAsociacion(null);
                        all_socias.remove(soc);
                        GestionBD.borrarJugador(soc);
                    } catch (NullPointerException ex) {
                        //Este jugador no tiene equipo, no hay que borrar la relacion
                        all_socias.remove(soc);
                        GestionBD.borrarJugador(soc);
                    }
                    txtNombre.setText("");
                    txtApellido.setText("");
                    txtID.setText("");


                    tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

                    soc = null;
                }
            }
        });

        /**
         * Usa un filtro con comboBox y una consulta para buscar un jugador de la tabla.
         * @see buscar();
         */
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soc = buscar(String.valueOf(comboBoxSocias.getSelectedItem()), txtBuscar.getText(), all_socias);

                if (soc == null) {
                    txtBuscar.setText("No encontrado.");
                    txtNombre.setText("");
                    txtApellido.setText("");
                    txtID.setText("");

                } else {
                    txtBuscar.setText("");
                    txtNombre.setText(soc.getNombre());
                    txtApellido.setText(soc.getApellidos());
                    txtID.setText(String.valueOf(soc.getId()));

                }
            }
        });

        buttonMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tupla = tableGestionSoc.getSelectedRow();
                if (tupla < 0) {
                    JOptionPane.showMessageDialog(null, "Elige una fila de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    soc = all_socias.get(tupla);

                    txtNombre.setText(soc.getNombre());
                    txtApellido.setText(soc.getApellidos());
                    txtID.setText(String.valueOf(soc.getId()));

                }
            }
        });
    }

    public Socia buscar(String filtro, String consulta, List<Socia> all_socias) {
        if (filtro.equals("ID")) {
            for (Socia socia : all_socias) {
                if (socia.getId(consulta)) {
                    return socia;
                }
            }
        } else if (filtro.equals("Nombre")) {
            for (Socia socia : all_socias) {
                if (socia.getNombre().equalsIgnoreCase(consulta)) {
                    return socia;
                }
            }
        } else {
            for (Socia socia : all_socias) {
                if (socia.getApellidos().equalsIgnoreCase(consulta)) {
                    return socia;
                }
            }
        }
        return null;
    }
}

