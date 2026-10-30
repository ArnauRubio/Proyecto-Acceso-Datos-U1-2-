package org.egibide.irepositories;

import org.egibide.models.Asociacion;

/**
 * Consulta asociaciones y completa sus listas con los datos proporcionados por los DAO.
 */
public interface AsociacionRepository {
    /**
     * Recupera una asociación y carga las listas de juegos y socias.
     *
     * @param id_asociacion identificador de la asociación
     * @return asociación con sus listas cargadas
     */
    Asociacion getAsociacion(int id_asociacion);
}
