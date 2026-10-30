package org.egibide.irepositories;

import org.egibide.models.Socia;

/**
 * Consulta socias junto con la información de sus relaciones.
 */
public interface SociaRepository {
    /**
     * Recupera una socia con su asociación y participaciones.
     *
     * @param id_socia identificador de la socia
     * @return socia con su asociación y participaciones cargadas
     */
    Socia getSocia(int id_socia);
}
