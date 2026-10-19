package org.egibide.idao;

import org.egibide.dao.PartidaDao;
import org.egibide.models.Partida;

import java.util.List;

public class PartidaDaoImpl implements PartidaDao {
    @Override
    public Partida getPartida(int id) {
        return null;
    }

    @Override
    public List<Partida> getPartidas() {
        return List.of();
    }

    @Override
    public int addPartida(Partida partida) {
        return 0;
    }

    @Override
    public boolean update(Partida partida) {
        return false;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Partida> getPartidasByJuegoId(int id_juego) {
        return List.of();
    }

    @Override
    public List<Partida> getPartidasBySociaId(int id_socia) {
        return List.of();
    }
}
