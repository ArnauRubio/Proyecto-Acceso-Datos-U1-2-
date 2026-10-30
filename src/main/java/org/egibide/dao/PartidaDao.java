package org.egibide.dao;

import org.egibide.models.Partida;

import java.sql.SQLException;
import java.util.List;

/**
 * Operaciones de persistencia y consulta de partidas.
 */
public interface PartidaDao {
    /**
     * Busca una partida por su identificador.
     *
     * @param id identificador de la partida
     * @return partida encontrada o null si no existe
     */
    Partida getPartida(int id);

    /**
     * Recupera todas las partidas registradas.
     *
     * @return lista de partidas, vacía si no hay resultados
     */
    List<Partida> getPartidas();

    /**
     * Inserta una partida mediante la operación básica del DAO.
     *
     * @param partida partida que se insertará
     * @return número de filas afectadas
     */
    int addPartida(Partida partida);

    /**
     * Actualiza una partida mediante la operación básica del DAO.
     *
     * @param partida datos actualizados
     * @return {@code true} si se modificó alguna fila
     */
    boolean update(Partida partida);

    /**
     * Elimina una partida por su identificador.
     *
     * @param id identificador de la partida que se eliminará
     */
    void delete(int id);

    /**
     * Obtiene las partidas asociadas a un juego.
     *
     * @param id_juego identificador del juego
     * @return partidas encontradas
     */
    List<Partida> getPartidasByJuegoId(int id_juego);

    /**
     * Obtiene la partida de una participación.
     *
     * @param id_participante identificador de la participación
     * @return partida encontrada o null si no existe
     */
    Partida getPartidaByParticipanteId(int id_participante);

    /**
     * Guarda la partida y reemplaza sus participantes dentro de una transacción.
     *
     * @param partida datos de la partida y participantes asociados
     * @param nueva   indica si se inserta en lugar de actualizarse
     * @throws SQLException si falla una operación; la transacción se revierte
     */
    void savePartida(Partida partida, boolean nueva) throws SQLException;

    /**
     * Elimina en una transacción los participantes y la partida.
     *
     * @param partida partida que se eliminará
     * @throws SQLException si falla una operación; la transacción se revierte
     */
    void deletePartida(Partida partida) throws SQLException;
}
