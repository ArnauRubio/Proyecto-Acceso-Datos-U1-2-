package org.egibide.dao;

import org.egibide.models.Juego;

import java.sql.SQLException;
import java.util.List;

/**
 * Operaciones de persistencia y consulta del catálogo de juegos.
 */
public interface JuegoDao {

    /**
     * Busca un juego por su identificador.
     *
     * @param id identificador del juego
     * @return juego encontrado o null si no existe
     */
    Juego getJuego(int id);

    /**
     * Recupera todos los juegos registrados.
     *
     * @return lista de juegos, vacía si no hay resultados
     */
    List<Juego> getJuegos();

    /**
     * Inserta un juego mediante la operación básica del DAO.
     *
     * @param juego datos del juego
     * @return número de filas afectadas
     */
    int addJuego(Juego juego);

    /**
     * Inserta un juego y resuelve su asociación si el esquema lo requiere.
     *
     * @param juego juego que se insertará
     * @return número de filas insertadas
     * @throws SQLException si falla la consulta o no puede resolverse la asociación
     */
    int insertJuego(Juego juego) throws SQLException;

    /**
     * Actualiza los datos de un juego y su asociación cuando corresponde.
     *
     * @param juego datos actualizados
     * @return {@code true} si se actualizó alguna fila
     * @throws SQLException si falla la consulta o la resolución de la asociación
     */
    boolean updateJuego(Juego juego) throws SQLException;

    /**
     * Actualiza un juego mediante la operación básica del DAO.
     *
     * @param juego datos actualizados
     * @return {@code true} si se modificó alguna fila
     */
    boolean update(Juego juego);

    /**
     * Elimina un juego por su identificador.
     *
     * @param id identificador del juego que se eliminará
     */
    void delete(int id);

    /**
     * Busca el juego vinculado a una partida.
     *
     * @param id_partida identificador de la partida
     * @return juego encontrado o null si no existe
     */
    Juego getJuegoByPartidaId(int id_partida);

}
