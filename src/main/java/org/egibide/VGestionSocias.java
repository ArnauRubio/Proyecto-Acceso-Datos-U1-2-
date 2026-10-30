package org.egibide;

import org.egibide.idao.SociaDaoImpl;
import org.egibide.idao.AsociacionDaoImpl;
import org.egibide.dao.AsociacionDao;
import org.egibide.models.Asociacion;
import org.egibide.models.Partida;
import org.egibide.models.Participante;
import org.egibide.models.Socia;
import org.egibide.utils.ModTabla_GesSoc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Pantalla Swing para consultar y mantener las socias de la asociación.
 */
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
    private JButton buttonLimpiar;
    private JTextPane textPane1;
    private JPanel panelGestionSoc;
    private JTextField txtEdad;
    private JTextField txtEmail;
    private JTextField txtAsociacion;

    private final SociaDaoImpl sociaDao = new SociaDaoImpl();
    private final AsociacionDao asociacionDao = new AsociacionDaoImpl();
    Socia soc = null;

    /**
     * Inicializa la tabla y los controles para crear, editar, buscar y borrar socias.
     *
     * @param all_socias lista compartida que se muestra y actualiza desde esta pantalla
     */
    public VGestionSocias(List<Socia> all_socias) {

        tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

        buttonLimpiar.setText("Limpiar");
        buttonLimpiar.addActionListener(e -> {
            clearForm();
            txtBuscar.setText("");
            tableGestionSoc.clearSelection();
        });

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

                try {
                    int edad = txtEdad.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtEdad.getText().trim());
                    Socia nuevoSoc = new Socia(id, nombre, apellido, edad, txtEmail.getText().trim());
                    Asociacion asociacion = asociacionDao.findAssociation(txtAsociacion.getText());
                    if (sociaDao.insertSocia(nuevoSoc, asociacion) == 0) {
                        showError("No se pudo guardar la socia.");
                        return;
                    }
                    nuevoSoc.setAsociacion(asociacion);
                    asociacion.getSocias().add(nuevoSoc);
                    all_socias.add(nuevoSoc);
                    JOptionPane.showMessageDialog(null, "Socia creada correctamente", "Nueva socia", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));
                    soc = null;
                } catch (java.sql.SQLException ex) {
                    showError("No se pudo guardar la socia: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    showError("La edad debe ser un número entero.");
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                }
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
                    if (idNuevo != idOriginal) {
                        showError("No se puede cambiar el ID de una socia existente.");
                        return;
                    }
                    try {
                        int edad = txtEdad.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtEdad.getText().trim());
                        Asociacion nuevaAsociacion = asociacionDao.findAssociation(txtAsociacion.getText());
                        Socia actualizada = new Socia(idNuevo, nombre, apellidos, edad, txtEmail.getText().trim());
                        if (!sociaDao.updateSocia(actualizada, nuevaAsociacion)) {
                            showError("No se encontró la socia en la base de datos.");
                            return;
                        }
                        Asociacion asociacionAnterior = soc.getAsociacion();
                        if (asociacionAnterior == null || asociacionAnterior.getId() != nuevaAsociacion.getId()) {
                            if (asociacionAnterior != null) {
                                asociacionAnterior.getSocias().remove(soc);
                            }
                            nuevaAsociacion.getSocias().add(soc);
                        }
                        soc.setNombre(nombre);
                        soc.setApellidos(apellidos);
                        soc.setEdad(edad);
                        soc.setEmail(txtEmail.getText().trim());
                        soc.setAsociacion(nuevaAsociacion);
                    } catch (java.sql.SQLException ex) {
                        showError("No se pudo actualizar la socia: " + ex.getMessage());
                        return;
                    } catch (NumberFormatException ex) {
                        showError("La edad debe ser un número entero.");
                        return;
                    } catch (IllegalArgumentException ex) {
                        showError(ex.getMessage());
                        return;
                    }

                    txtNombre.setText("");
                    txtApellido.setText("");
                    txtID.setText("");
                    txtEdad.setText("");
                    txtEmail.setText("");
                    txtAsociacion.setText("");
                    textPane1.setText("");

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
                    txtEdad.setText("");
                    txtEmail.setText("");
                    txtAsociacion.setText("");
                    textPane1.setText("");


                    tableGestionSoc.setModel(new ModTabla_GesSoc(all_socias));

                    soc = null;
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soc = buscar(String.valueOf(comboBoxSocias.getSelectedItem()), txtBuscar.getText().trim(), all_socias);

                if (soc == null) {
                    txtBuscar.setText("No encontrado.");
                    clearForm();

                } else {
                    txtBuscar.setText("");
                    showSocia(soc);

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

                    showSocia(soc);

                }
            }
        });
        buttonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    /**
     * Busca una socia por ID, nombre o apellidos según el filtro seleccionado.
     *
     * @param filtro     criterio de búsqueda: {@code ID}, {@code Nombre} o apellidos
     * @param consulta   texto que se compara con el criterio
     * @param all_socias socias en las que se realiza la búsqueda
     * @return primera socia coincidente o null si no hay coincidencias
     */
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

    /**
     * Busca una socia por ID en la lista cargada en la pantalla.
     */
    private Socia findById(List<Socia> socias, int id) {
        for (Socia socia : socias) {
            if (socia.getId() == id) {
                return socia;
            }
        }
        return null;
    }

    /**
     * Muestra un mensaje de error asociado a esta ventana.
     */
    private void showError(String mensaje) {
        JOptionPane.showMessageDialog(panelSoc, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Rellena el formulario y el resumen de partidas con los datos de una socia.
     */
    private void showSocia(Socia socia) {
        txtNombre.setText(socia.getNombre());
        txtApellido.setText(socia.getApellidos());
        txtID.setText(String.valueOf(socia.getId()));
        txtEdad.setText(String.valueOf(socia.getEdad()));
        txtEmail.setText(socia.getEmail() == null ? "" : socia.getEmail());
        Asociacion asociacion = socia.getAsociacion();
        txtAsociacion.setText(asociacion == null ? "Sin asociación" : asociacion.getNombre());

        StringBuilder partidas = new StringBuilder();
        for (Participante participante : socia.getParticipaciones()) {
            Partida partida = participante.getPartida();
            if (partida == null) {
                continue;
            }
            if (partidas.length() > 0) {
                partidas.append('\n');
            }
            partidas.append(partida.getJuego() == null ? "Juego sin nombre" : partida.getJuego().getNombre())
                    .append(" - ").append(partida.getFecha()).append(" a las ").append(partida.getHora());
        }
        textPane1.setText(partidas.length() == 0 ? "Sin partidas" : partidas.toString());
    }

    /**
     * Limpia los campos del formulario y descarta la socia seleccionada.
     */
    private void clearForm() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtID.setText("");
        txtEdad.setText("");
        txtEmail.setText("");
        txtAsociacion.setText("");
        textPane1.setText("");
        soc = null;
    }
}
