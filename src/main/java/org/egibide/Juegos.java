package org.egibide;

import org.egibide.models.Juego;
import org.egibide.models.ModoJuego;
import org.egibide.models.Socia;
import org.egibide.idao.JuegoDaoImpl;
import org.egibide.utils.ModTabla_GesJuegos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class Juegos {
    JPanel panelGestionJuegos;
    private JPanel panelGestionSoc;
    private JPanel panelSoc;
    private JTable tableGestionSoc;
    private JButton borrarButton;
    private JButton editarButton;
    private JButton nuevoButton;
    private JTextField txtBuscar;
    private JButton buscarButton;
    private JComboBox<String> comboBoxJuegos;
    private JTextField txtID;
    private JTextField txtCoopComp;
    private JTextField txtNombre;
    private JButton buttonMostrar;
    private JTextPane textPane1;
    private JTextField txtJugadoresDeseados;
    private JTextField txtDuracion;
    private JTextField txtMinJugadores;
    private JLabel duracion;
    private JTextField txtMaxJugadores;
    private JTextField txtDueno;
    private JTextField txtLocalizacion;
    private JTextField txtTematica;

    private final List<Juego> allJuegos;
    private final List<Socia> allSocias;
    private final JuegoDaoImpl juegoDao = new JuegoDaoImpl();
    private Juego juegoSeleccionado;

    public Juegos(List<Juego> allJuegos, List<Socia> allSocias) {
        this.allJuegos = allJuegos;
        this.allSocias = allSocias;
        tableGestionSoc.setModel(new ModTabla_GesJuegos(allJuegos));

        buttonMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableGestionSoc.getSelectedRow();
                if (selectedRow < 0) {
                    showError("Selecciona un juego de la tabla.");
                    return;
                }
                showJuego(allJuegos.get(tableGestionSoc.convertRowIndexToModel(selectedRow)));
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarJuego();
            }
        });

        nuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Juego nuevo = juegoDesdeFormulario();
                    if (findById(nuevo.getId()) != null) {
                        showError("Ya existe un juego con ese ID.");
                        return;
                    }
                    if (juegoDao.addJuego(nuevo) == 0) {
                        showError("No se pudo guardar el juego.");
                        return;
                    }
                    allJuegos.add(nuevo);
                    refreshTable();
                    clearForm();
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoSeleccionado == null) {
                    showError("Busca o muestra un juego antes de editarlo.");
                    return;
                }
                try {
                    Juego actualizado = juegoDesdeFormulario();
                    Juego existente = findById(actualizado.getId());
                    if (existente != null && existente != juegoSeleccionado) {
                        showError("Ya existe un juego con ese ID.");
                        return;
                    }
                    if (actualizado.getId() != juegoSeleccionado.getId()) {
                        showError("No se puede cambiar el ID de un juego existente.");
                        return;
                    }
                    actualizado.setAutor(juegoSeleccionado.getAutor());
                    actualizado.setDescripcion(juegoSeleccionado.getDescripcion());
                    actualizado.setFechaAlta(juegoSeleccionado.getFechaAlta());
                    if (!juegoDao.update(actualizado)) {
                        showError("No se encontró el juego en la base de datos.");
                        return;
                    }
                    juegoSeleccionado.setId(actualizado.getId());
                    juegoSeleccionado.setNombre(actualizado.getNombre());
                    juegoSeleccionado.setAutor(actualizado.getAutor());
                    juegoSeleccionado.setDescripcion(actualizado.getDescripcion());
                    juegoSeleccionado.setModoJuego(actualizado.getModoJuego());
                    juegoSeleccionado.setMinJugadores(actualizado.getMinJugadores());
                    juegoSeleccionado.setMaxJugadores(actualizado.getMaxJugadores());
                    juegoSeleccionado.setDuracion(actualizado.getDuracion());
                    juegoSeleccionado.setDueno(actualizado.getDueno());
                    juegoSeleccionado.setLocalizacion(actualizado.getLocalizacion());
                    juegoSeleccionado.setTematica(actualizado.getTematica());
                    refreshTable();
                    clearForm();
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoSeleccionado == null) {
                    showError("Busca o muestra un juego antes de borrarlo.");
                    return;
                }
                if (!juegoSeleccionado.getPartidas().isEmpty()) {
                    showError("No se puede borrar un juego que tiene partidas asociadas.");
                    return;
                }
                int confirmacion = JOptionPane.showConfirmDialog(panelGestionJuegos,
                        "¿Borrar el juego " + juegoSeleccionado.getNombre() + "?",
                        "Confirmar borrado", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    juegoDao.delete(juegoSeleccionado.getId());
                    allJuegos.remove(juegoSeleccionado);
                    refreshTable();
                    clearForm();
                }
            }
        });
    }

    private Juego juegoDesdeFormulario() {
        String nombre = txtNombre.getText().trim();
        String modo = txtCoopComp.getText().trim();
        String propietarioTexto = txtDueno.getText().trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del juego es obligatorio.");
        }
        if (modo.isEmpty()) {
            throw new IllegalArgumentException("Indica el modo cooperativo o competitivo.");
        }

        try {
            int id = Integer.parseInt(txtID.getText().trim());
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            int minJugadores = Integer.parseInt(txtMinJugadores.getText().trim());
            int maxJugadores = Integer.parseInt(txtMaxJugadores.getText().trim());
            if (duracion <= 0 || minJugadores <= 0 || maxJugadores < minJugadores) {
                throw new IllegalArgumentException("Duración y jugadores deben ser válidos; el máximo no puede ser menor que el mínimo.");
            }
            ModoJuego modoJuego;
            try {
                modoJuego = ModoJuego.valueOf(modo.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("El modo debe ser COOPERATIVO o COMPETITIVO.");
            }
            Socia propietaria = buscarPropietaria(propietarioTexto);
            if (!propietarioTexto.isEmpty() && propietaria == null) {
                throw new IllegalArgumentException("No se encontró una socia con ese ID o nombre.");
            }
            Juego juego = new Juego(id, nombre, "", "", minJugadores, maxJugadores, duracion,
                    txtTematica.getText().trim(), modoJuego, txtLocalizacion.getText().trim(),
                    propietaria, Date.valueOf(java.time.LocalDate.now()));
            return juego;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID, duración y número de jugadores deben ser números enteros.");
        }
    }

    private Socia buscarPropietaria(String consulta) {
        for (Socia socia : allSocias) {
            if (String.valueOf(socia.getId()).equals(consulta)
                    || (socia.getNombre() + " " + socia.getApellidos()).equalsIgnoreCase(consulta)) {
                return socia;
            }
        }
        return null;
    }

    private Juego findById(int id) {
        for (Juego juego : allJuegos) {
            if (juego.getId() == id) {
                return juego;
            }
        }
        return null;
    }

    private void buscarJuego() {
        String consulta = txtBuscar.getText().trim();
        if (consulta.isEmpty()) {
            showError("Escribe un valor para buscar.");
            return;
        }
        String filtro = String.valueOf(comboBoxJuegos.getSelectedItem());
        for (int i = 0; i < allJuegos.size(); i++) {
            Juego juego = allJuegos.get(i);
            String valor;
            switch (filtro) {
                case "ID":
                    valor = String.valueOf(juego.getId());
                    break;
                case "Nombre":
                    valor = juego.getNombre();
                    break;
                case "Duracion":
                    valor = String.valueOf(juego.getDuracion());
                    break;
                case "Jugadores Max.":
                    valor = String.valueOf(juego.getMaxJugadores());
                    break;
                case "Jugadores Min.":
                    valor = String.valueOf(juego.getMinJugadores());
                    break;
                case "Tematica":
                    valor = juego.getTematica();
                    break;
                default:
                    continue;
            }
            if (valor != null && valor.toLowerCase().contains(consulta.toLowerCase())) {
                tableGestionSoc.setRowSelectionInterval(i, i);
                tableGestionSoc.scrollRectToVisible(tableGestionSoc.getCellRect(i, 0, true));
                showJuego(juego);
                return;
            }
        }
        showError("No se encontró ningún juego.");
    }

    private void showJuego(Juego juego) {
        juegoSeleccionado = juego;
        txtID.setText(String.valueOf(juego.getId()));
        txtNombre.setText(juego.getNombre());
        txtCoopComp.setText(juego.getModoJuego() == null ? "" : juego.getModoJuego().name());
        txtDuracion.setText(String.valueOf(juego.getDuracion()));
        txtMinJugadores.setText(String.valueOf(juego.getMinJugadores()));
        txtMaxJugadores.setText(String.valueOf(juego.getMaxJugadores()));
        txtJugadoresDeseados.setText(juego.getMinJugadores() + " - " + juego.getMaxJugadores());
        txtDueno.setText(juego.getDueno() == null ? "" : String.valueOf(juego.getDueno().getId()));
        txtLocalizacion.setText(juego.getLocalizacion() == null ? "" : juego.getLocalizacion());
        txtTematica.setText(juego.getTematica() == null ? "" : juego.getTematica());
        textPane1.setText(String.valueOf(juego.getPartidas().size()));
    }

    private void refreshTable() {
        tableGestionSoc.setModel(new ModTabla_GesJuegos(allJuegos));
    }

    private void clearForm() {
        txtID.setText("");
        txtNombre.setText("");
        txtCoopComp.setText("");
        txtDuracion.setText("");
        txtMinJugadores.setText("");
        txtMaxJugadores.setText("");
        txtJugadoresDeseados.setText("");
        txtDueno.setText("");
        txtLocalizacion.setText("");
        txtTematica.setText("");
        txtBuscar.setText("");
        textPane1.setText("");
        juegoSeleccionado = null;
    }

    private void showError(String mensaje) {
        JOptionPane.showMessageDialog(panelGestionJuegos, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
