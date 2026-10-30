package org.egibide.dao;

import org.egibide.models.Participante;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;

/**
 * Operaciones de persistencia y consulta de las participaciones en partidas.
 */
public interface ParticipanteDao {
    /**
     * Busca una participación por su identificador.
     *
     * @param id identificador de la participación
     * @return participación encontrada o null si no existe
     */
    Participante getParticipante(int id);

    /**
     * Recupera todas las participaciones registradas.
     *
     * @return lista de participaciones, vacía si no hay resultados
     */
    List<Participante> getParticipantes();

    /**
     * Inserta una participación.
     *
     * @param participante datos que se insertarán
     * @return número de filas afectadas
     */
    int addParticipante(Participante participante);

    /**
     * Actualiza una participación.
     *
     * @param participante datos actualizados
     * @return {@code true} si se modificó alguna fila
     */
    boolean update(Participante participante);

    /**
     * Elimina una participación por su identificador.
     *
     * @param id identificador de la participación que se eliminará
     */
    void delete(int id);

    /**
     * Obtiene las participantes de una partida.
     *
     * @param id_partida identificador de la partida
     * @return participaciones encontradas
     */
    List<Participante> getParticipantesByPartidaId(int id_partida);

    /**
     * Obtiene las participantes ganadoras de una partida.
     *
     * @param id_partida identificador de la partida
     * @return participaciones ganadoras
     */
    List<Participante> getGanadorasByPartidaId(int id_partida);

    /**
     * Obtiene las participaciones de una socia.
     *
     * @param id_socia identificador de la socia
     * @return participaciones encontradas
     */
    List<Participante> getParticipacionesBySociaId(int id_socia);

    /**
     * Enlaza las participaciones cargadas con sus partidas y socias correspondientes.
     *
     * @param partidas partidas previamente cargadas
     * @param socias   socias previamente cargadas
     * @throws SQLException si una referencia no se puede resolver o falla la consulta
     */
    void loadParticipants(List<Partida> partidas, List<Socia> socias) throws SQLException;
}
