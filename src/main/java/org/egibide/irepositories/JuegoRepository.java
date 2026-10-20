package org.egibide.irepositories;

import org.egibide.models.Asociacion;
import org.egibide.models.Juego;

public interface JuegoRepository {
    Juego getJuego(int id_juego);
}
