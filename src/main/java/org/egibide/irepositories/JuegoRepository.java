package org.egibide.irepositories;

import org.egibide.models.Juego;

/**
 * Consulta juegos completados con sus relaciones principales.
 */
public interface JuegoRepository {
    /**
     * Recupera un juego con su asociación y sus partidas.
     *
     * @param id_juego identificador del juego
     * @return juego con la asociación y las partidas cargadas
     */
    Juego getJuego(int id_juego);
}
