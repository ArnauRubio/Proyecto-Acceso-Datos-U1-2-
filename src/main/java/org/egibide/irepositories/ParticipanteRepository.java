package org.egibide.irepositories;

import org.egibide.models.Participante;

/**
 * Consulta participaciones junto con las entidades a las que hacen referencia.
 */
public interface ParticipanteRepository {
    /**
     * Recupera una participación con su socia y partida.
     *
     * @param id_participante identificador de la participación
     * @return participación con la socia y la partida cargadas
     */
    Participante getParticipante(int id_participante);
}
