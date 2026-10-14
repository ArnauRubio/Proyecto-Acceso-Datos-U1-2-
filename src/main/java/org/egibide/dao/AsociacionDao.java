package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;

import java.util.List;

public interface AsociacionDao {
    Asociacion getAsociacion(int id);

    boolean update(Asociacion asociacion);

    void delete(int id);

    //Asociacion getAsociacionBySociaId(int id_socia);
}
