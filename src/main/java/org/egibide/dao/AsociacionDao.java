package org.egibide.dao;

import org.egibide.models.Asociacion;

import java.sql.SQLException;

/**
 * Acceso a los datos persistidos de asociaciones.
 */
public interface AsociacionDao {
    /**
     * Busca una asociación por su identificador.
     *
     * @param id identificador de la asociación
     * @return asociación encontrada o null si no existe
     */
    Asociacion getAsociacion(int id);

    /**
     * Busca una asociación por su ID o por su nombre sin distinguir mayúsculas.
     *
     * @param value identificador o nombre de la asociación
     * @return asociación encontrada
     * @throws SQLException si falla la consulta a la base de datos
     */
    Asociacion findAssociation(String value) throws SQLException;

    /**
     * Actualiza los datos de una asociación existente.
     *
     * @param asociacion asociación con los datos actualizados
     * @return {@code true} si se modificó alguna fila
     */
    boolean update(Asociacion asociacion);

    /**
     * Elimina una asociación por su identificador.
     *
     * @param id identificador de la asociación que se eliminará
     */
    void delete(int id);

    /**
     * Obtiene la asociación propietaria de un juego.
     *
     * @param id_juego identificador del juego
     * @return asociación encontrada o null si no existe
     */
    Asociacion getAsociacionByJuegoId(int id_juego);

    /**
     * Obtiene la asociación a la que pertenece una socia.
     *
     * @param id_socia identificador de la socia
     * @return asociación encontrada o null si no existe
     */
    Asociacion getAsociacionBySociaId(int id_socia);
}
