package org.egibide.dao;

import org.egibide.models.Juego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.util.List;

public interface PartidaDao {
    Partida getPartida(int id);

    List<Partida> getPartidas();

    int addPartida(Partida partida);

    boolean update(Partida partida);

    void delete(int id);

    List<Partida> getPartidasByJuegoId(int id_juego);

    List<Partida> getPartidasBySociaId(int id_socia);
}
