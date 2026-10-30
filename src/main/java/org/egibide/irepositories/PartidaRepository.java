package org.egibide.irepositories;

import org.egibide.models.Partida;

/**
 * Consulta partidas junto con el juego y las listas de participantes.
 */
public interface PartidaRepository {
    /**
     * Recupera una partida con su juego, participantes y ganadoras.
     *
     * @param id_partida identificador de la partida
     * @return partida con el juego y las listas de participantes y ganadoras cargadas
     */
    Partida getPartida(int id_partida);
}
