package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;

public interface JuegoDao {
    Juego getJuego(int id);

    List<Juego> getJuegos();

    int addJuego(Juego juego) throws SQLException;

    boolean update(Juego juego, int idOriginal) throws SQLException;

    boolean delete(int id) throws SQLException;

    //List<Juego> getJuegosByAsociacionId(int id_asociacion);
}
