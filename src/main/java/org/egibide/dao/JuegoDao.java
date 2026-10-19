package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;

import java.util.List;

public interface JuegoDao {
    Juego getJuego(int id);

    List<Juego> getJuegos();

    int addJuego(Juego juego);

    boolean update(Juego juego);

    void delete(int id);

    //List<Juego> getJuegosByAsociacionId(int id_asociacion);
}
