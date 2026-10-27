package org.egibide;

import org.egibide.idao.SociaDaoImpl;
import org.egibide.models.Socia;
import org.egibide.utils.ModTabla_GesSoc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VGestionSocias {
    public JPanel panelSoc;
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
    private JPanel panelGestionSoc;

    private final SociaDaoImpl sociaDao = new SociaDaoImpl();
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
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                int id;
                try {
                    id = Integer.parseInt(txtID.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El ID debe ser un número entero.",
                            "Dato no válido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nombre.isEmpty() || apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre y los apellidos son obligatorios.",
                            "Dato no válido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (findById(all_socias, id) != null) {
                    JOptionPane.showMessageDialog(null, "Ya existe una socia con ese ID.",
                            "Dato no válido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Socia nuevoSoc = new Socia(id, nombre, apellido, 0, "");
                if (sociaDao.addSocia(nuevoSoc) == 0) {
                    showError("No se pudo guardar la socia.");
                    return;
                }

                all_socias.add(nuevoSoc);
                JOptionPane.showMessageDialog(null, "Socia creada correctamente", "Nueva socia", JOptionPane.INFORMATION_MESSAGE);

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

                if (soc == null) {
                    JOptionPane.showMessageDialog(null, "Busca o muestra (>) una socia", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int idOriginal = soc.getId();
                    String nombre = txtNombre.getText().trim();
                    String apellidos = txtApellido.getText().trim();
                    int idNuevo;
                    try {
                        idNuevo = Integer.parseInt(txtID.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "El ID debe ser un número entero.",
                                "Dato no válido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (nombre.isEmpty() || apellidos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "El nombre y los apellidos son obligatorios.",
                                "Dato no válido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Socia existente = findById(all_socias, idNuevo);
                    if (existente != null && existente != soc) {
                        showError("Ya existe una socia con ese ID.");
                        return;
                    }
                    Socia actualizada = new Socia(idNuevo, nombre, apellidos, soc.getEdad(), soc.getEmail());
                    if (idNuevo != idOriginal) {
                        showError("No se puede cambiar el ID de una socia existente.");
                        return;
                    }
                    if (!sociaDao.update(actualizada)) {
                        showError("No se encontró la socia en la base de datos.");
                        return;
                    }

                    soc.setNombre(nombre);
                    soc.setApellidos(apellidos);
                    soc.setId(idNuevo);

                    txtNombre.setText("");
                    txtApellido.setText("");
                    txtID.setText("");

                    tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

                    soc = null;
                }
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (soc == null) {
                    JOptionPane.showMessageDialog(null, "Busca o muestra (>) un jugador", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    sociaDao.delete(soc.getId());

                    if (soc.getAsociacion() != null) {
                        soc.getAsociacion().getSocias().remove(soc);
                    }
                    all_socias.remove(soc);
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
                soc = buscar(String.valueOf(comboBoxSocias.getSelectedItem()), txtBuscar.getText().trim(), all_socias);

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
                    soc = all_socias.get(tableGestionSoc.convertRowIndexToModel(tupla));

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
                if (String.valueOf(socia.getId()).equals(consulta)) {
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

    private Socia findById(List<Socia> socias, int id) {
        for (Socia socia : socias) {
            if (socia.getId() == id) {
                return socia;
            }
        }
        return null;
    }

    private void showError(String mensaje) {
        JOptionPane.showMessageDialog(panelSoc, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
