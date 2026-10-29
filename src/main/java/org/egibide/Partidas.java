package org.egibide;

import org.egibide.models.Juego;
import org.egibide.models.Participante;
import org.egibide.models.Partida;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseRelations;
import org.egibide.utils.ModTabla_GesPartidas;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Partidas {
    private JTable table1;
    private JButton buttonEditar;
    private JButton buttonBorrar;
    private JButton buttonNueva;
    private JComboBox<String> comboBoxFiltroPartidas;
    private JButton ButtonFiltrar;
    JPanel panelGestionPartidas;

    private final List<Partida> allPartidas;
    private final List<Juego> allJuegos;
    private final List<Socia> allSocias;
    private final ModTabla_GesPartidas tableModel;
    private final TableRowSorter<ModTabla_GesPartidas> sorter;

    public Partidas(List<Partida> allPartidas, List<Juego> allJuegos, List<Socia> allSocias) {
        this.allPartidas = allPartidas;
        this.allJuegos = allJuegos;
        this.allSocias = allSocias;
        this.tableModel = new ModTabla_GesPartidas(allPartidas);
        table1.setModel(tableModel);
        table1.getColumnModel().getColumn(5).setCellRenderer(new ParticipantesRenderer());
        this.sorter = new TableRowSorter<>(tableModel);
        table1.setRowSorter(sorter);

        buttonNueva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Partida partida = editarDatosPartida(null);
                if (partida != null) {
                    try {
                        DatabaseRelations.savePartida(partida, true);
                    } catch (java.sql.SQLException ex) {
                        mostrarError("No se pudo guardar la partida y sus participantes: " + ex.getMessage());
                        return;
                    }
                    allPartidas.add(partida);
                    if (partida.getJuego() != null) {
                        partida.getJuego().getPartidas().add(partida);
                    }
                    vincularParticipantes(partida);
                    tableModel.fireTableDataChanged();
                }
            }
        });

        buttonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Partida seleccionada = partidaSeleccionada();
                if (seleccionada == null) {
                    mostrarError("Selecciona una partida para editar.");
                    return;
                }
                Partida cambios = editarDatosPartida(seleccionada);
                if (cambios != null) {
                    try {
                        DatabaseRelations.savePartida(cambios, false);
                    } catch (java.sql.SQLException ex) {
                        mostrarError("No se pudieron guardar los cambios de la partida: " + ex.getMessage());
                        return;
                    }
                    Juego juegoAnterior = seleccionada.getJuego();
                    seleccionada.setFecha(cambios.getFecha());
                    seleccionada.setHora(cambios.getHora());
                    seleccionada.setJugadoresDeseados(cambios.getJugadoresDeseados());
                    seleccionada.setJuego(cambios.getJuego());
                    desvincularParticipantes(seleccionada);
                    seleccionada.setJugadoras(cambios.getJugadoras());
                    for (Participante participante : seleccionada.getJugadoras()) {
                        participante.setPartida(seleccionada);
                    }
                    seleccionada.setGanadoras(cambios.getGanadoras());
                    vincularParticipantes(seleccionada);
                    if (juegoAnterior != null && juegoAnterior != cambios.getJuego()) {
                        juegoAnterior.getPartidas().remove(seleccionada);
                    }
                    if (cambios.getJuego() != null && !cambios.getJuego().getPartidas().contains(seleccionada)) {
                        cambios.getJuego().getPartidas().add(seleccionada);
                    }
                    tableModel.fireTableDataChanged();
                }
            }
        });

        buttonBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Partida seleccionada = partidaSeleccionada();
                if (seleccionada == null) {
                    mostrarError("Selecciona una partida para borrar.");
                    return;
                }
                int respuesta = JOptionPane.showConfirmDialog(panelGestionPartidas,
                        "¿Borrar la partida " + seleccionada.getId() + "?", "Confirmar borrado",
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        DatabaseRelations.deletePartida(seleccionada);
                    } catch (java.sql.SQLException ex) {
                        mostrarError("No se pudo borrar la partida: " + ex.getMessage());
                        return;
                    }
                    allPartidas.remove(seleccionada);
                    if (seleccionada.getJuego() != null) {
                        seleccionada.getJuego().getPartidas().remove(seleccionada);
                    }
                    desvincularParticipantes(seleccionada);
                    tableModel.fireTableDataChanged();
                }
            }
        });

        ButtonFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sorter.getRowFilter() != null) {
                    sorter.setRowFilter(null);
                    ButtonFiltrar.setText("Filtrar");
                    return;
                }
                aplicarFiltro();
            }
        });
        ButtonFiltrar.setText("Filtrar");
    }

    private Partida partidaSeleccionada() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return allPartidas.get(table1.convertRowIndexToModel(selectedRow));
    }

    private Partida editarDatosPartida(Partida partidaOriginal) {
        if (allJuegos.isEmpty()) {
            mostrarError("Añade al menos un juego antes de crear una partida.");
            return null;
        }

        JComboBox<Juego> juegos = new JComboBox<>(allJuegos.toArray(new Juego[0]));
        juegos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value instanceof Juego ? ((Juego) value).getNombre() : "");
                return this;
            }
        });
        JTextField fecha = new JTextField(partidaOriginal == null || partidaOriginal.getFecha() == null
                ? new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                : new SimpleDateFormat("yyyy-MM-dd").format(partidaOriginal.getFecha()));
        JTextField hora = new JTextField(partidaOriginal == null ? "18" : String.valueOf(partidaOriginal.getHora()));
        JTextField jugadores = new JTextField(partidaOriginal == null ? "2"
                : String.valueOf(partidaOriginal.getJugadoresDeseados()));
        JList<Socia> participantes = new JList<>(allSocias.toArray(new Socia[0]));
        participantes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        participantes.setVisibleRowCount(Math.min(4, Math.max(1, allSocias.size())));
        participantes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Socia socia = (Socia) value;
                setText(socia.getId() + " - " + socia.getNombre() + " " + socia.getApellidos());
                return this;
            }
        });
        JList<Socia> ganadoras = new JList<>(allSocias.toArray(new Socia[0]));
        ganadoras.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ganadoras.setVisibleRowCount(Math.min(4, Math.max(1, allSocias.size())));
        ganadoras.setCellRenderer(participantes.getCellRenderer());
        if (partidaOriginal != null && partidaOriginal.getJuego() != null) {
            juegos.setSelectedItem(partidaOriginal.getJuego());
        }
        if (partidaOriginal != null) {
            List<Integer> indicesSeleccionados = new ArrayList<>();
            List<Integer> indicesGanadoras = new ArrayList<>();
            for (Participante participante : partidaOriginal.getJugadoras()) {
                if (participante.getSocia() != null) {
                    int indice = allSocias.indexOf(participante.getSocia());
                    if (indice >= 0) {
                        indicesSeleccionados.add(indice);
                        if (participante.isGanadora()) {
                            indicesGanadoras.add(indice);
                        }
                    }
                }
            }
            participantes.setSelectedIndices(indicesSeleccionados.stream().mapToInt(Integer::intValue).toArray());
            ganadoras.setSelectedIndices(indicesGanadoras.stream().mapToInt(Integer::intValue).toArray());
        }

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.add(new JLabel("Juego:"));
        formulario.add(juegos);
        formulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formulario.add(fecha);
        formulario.add(new JLabel("Hora (0-23):"));
        formulario.add(hora);
        formulario.add(new JLabel("Jugadores deseados:"));
        formulario.add(jugadores);
        formulario.add(new JLabel("Participantes:"));
        formulario.add(new JScrollPane(participantes));
        formulario.add(new JLabel("Ganadoras:"));
        formulario.add(new JScrollPane(ganadoras));

        while (JOptionPane.showConfirmDialog(panelGestionPartidas, formulario,
                partidaOriginal == null ? "Nueva partida" : "Editar partida",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                Date fechaPartida = parseFecha(fecha.getText().trim());
                int horaPartida = Integer.parseInt(hora.getText().trim());
                int jugadoresDeseados = Integer.parseInt(jugadores.getText().trim());
                if (horaPartida < 0 || horaPartida > 23 || jugadoresDeseados <= 0) {
                    throw new IllegalArgumentException("La hora debe estar entre 0 y 23 y los jugadores deben ser mayor que cero.");
                }
                Juego juegoElegido = (Juego) juegos.getSelectedItem();
                if (juegoElegido == null) {
                    throw new IllegalArgumentException("Selecciona un juego para la partida.");
                }
                if (jugadoresDeseados < juegoElegido.getMinJugadores()
                        || jugadoresDeseados > juegoElegido.getMaxJugadores()) {
                    throw new IllegalArgumentException("El número de jugadores debe estar entre "
                            + juegoElegido.getMinJugadores() + " y " + juegoElegido.getMaxJugadores() + ".");
                }
                if (participantes.getSelectedValuesList().size() > jugadoresDeseados) {
                    throw new IllegalArgumentException("No puede haber más participantes que jugadores deseados.");
                }
                if (!participantes.getSelectedValuesList().containsAll(ganadoras.getSelectedValuesList())) {
                    throw new IllegalArgumentException("Solo pueden ser ganadoras las participantes seleccionadas.");
                }
                int id = partidaOriginal == null ? siguienteId() : partidaOriginal.getId();
                Partida resultado = new Partida(id, new java.sql.Date(fechaPartida.getTime()),
                        horaPartida, jugadoresDeseados);
                resultado.setJuego(juegoElegido);
                resultado.setJugadoras(crearParticipantes(participantes.getSelectedValuesList(),
                        ganadoras.getSelectedValuesList(), partidaOriginal, resultado));
                List<Participante> ganadorasSeleccionadas = new ArrayList<>();
                for (Participante participante : resultado.getJugadoras()) {
                    if (participante.isGanadora()) {
                        ganadorasSeleccionadas.add(participante);
                    }
                }
                resultado.setGanadoras(ganadorasSeleccionadas);
                return resultado;
            } catch (NumberFormatException ex) {
                mostrarError("La hora y el número de jugadores deben ser números enteros.");
            } catch (IllegalArgumentException ex) {
                mostrarError(ex.getMessage());
            } catch (ParseException ex) {
                mostrarError("La fecha debe tener el formato AAAA-MM-DD y ser válida.");
            }
        }
        return null;
    }

    private Date parseFecha(String texto) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setLenient(false);
        ParsePosition posicion = new ParsePosition(0);
        Date fecha = formato.parse(texto, posicion);
        if (fecha == null || posicion.getIndex() != texto.length()) {
            throw new ParseException("Fecha no válida", posicion.getErrorIndex());
        }
        return fecha;
    }

    private int siguienteId() {
        int idMaximo = 0;
        for (Partida partida : allPartidas) {
            idMaximo = Math.max(idMaximo, partida.getId());
        }
        return idMaximo + 1;
    }

    private List<Participante> crearParticipantes(List<Socia> socias, List<Socia> ganadoras,
                                                  Partida partidaOriginal, Partida partida) {
        List<Participante> participantes = new ArrayList<>();
        int id = siguienteIdParticipante();
        for (Socia socia : socias) {
            Participante existente = buscarParticipante(partidaOriginal, socia);
            Participante participante = existente == null ? new Participante(id++, false) : existente;
            participante.setSocia(socia);
            participante.setPartida(partida);
            participante.setGanadora(ganadoras.contains(socia));
            participantes.add(participante);
        }
        return participantes;
    }

    private Participante buscarParticipante(Partida partida, Socia socia) {
        if (partida == null) {
            return null;
        }
        for (Participante participante : partida.getJugadoras()) {
            if (participante.getSocia() != null && participante.getSocia().getId() == socia.getId()) {
                return participante;
            }
        }
        return null;
    }

    private int siguienteIdParticipante() {
        int idMaximo = 0;
        for (Partida partida : allPartidas) {
            for (Participante participante : partida.getJugadoras()) {
                idMaximo = Math.max(idMaximo, participante.getId());
            }
        }
        return idMaximo + 1;
    }

    private void vincularParticipantes(Partida partida) {
        for (Participante participante : partida.getJugadoras()) {
            participante.setPartida(partida);
            Socia socia = participante.getSocia();
            if (socia != null && !socia.getParticipaciones().contains(participante)) {
                socia.getParticipaciones().add(participante);
            }
        }
    }

    private void desvincularParticipantes(Partida partida) {
        for (Participante participante : partida.getJugadoras()) {
            Socia socia = participante.getSocia();
            if (socia != null) {
                socia.getParticipaciones().remove(participante);
            }
        }
    }

    private void aplicarFiltro() {
        String filtro = String.valueOf(comboBoxFiltroPartidas.getSelectedItem());
        if ("Disponibles".equals(filtro)) {
            sorter.setRowFilter(new RowFilter<ModTabla_GesPartidas, Integer>() {
                @Override
                public boolean include(Entry<? extends ModTabla_GesPartidas, ? extends Integer> entry) {
                    Partida partida = allPartidas.get(entry.getIdentifier());
                    return partida.getJugadoras().size() < partida.getJugadoresDeseados();
                }
            });
        } else {
            String valor = JOptionPane.showInputDialog(panelGestionPartidas,
                    "Valor de " + filtro + " que quieres buscar:", "Filtrar partidas", JOptionPane.QUESTION_MESSAGE);
            if (valor == null) {
                return;
            }
            valor = valor.trim();
            if (valor.isEmpty()) {
                mostrarError("Escribe un valor para filtrar.");
                return;
            }
            final String consulta = valor.toLowerCase();
            final int columna;
            switch (filtro) {
                case "Juego":
                    columna = 1;
                    break;
                case "Fecha":
                    columna = 2;
                    break;
                case "Hora":
                    columna = 3;
                    break;
                default:
                    return;
            }
            sorter.setRowFilter(new RowFilter<ModTabla_GesPartidas, Integer>() {
                @Override
                public boolean include(Entry<? extends ModTabla_GesPartidas, ? extends Integer> entry) {
                    return entry.getStringValue(columna).toLowerCase().contains(consulta);
                }
            });
        }
        ButtonFiltrar.setText("Limpiar filtro");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panelGestionPartidas, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class ParticipantesRenderer extends JTextArea implements TableCellRenderer {
        private ParticipantesRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setFont(table.getFont());
            setSize(table.getColumnModel().getColumn(column).getWidth(), Integer.MAX_VALUE);
            int height = Math.max(table.getRowHeight(), getPreferredSize().height);
            if (table.getRowHeight(row) != height) {
                table.setRowHeight(row, height);
            }
            return this;
        }
    }
}
