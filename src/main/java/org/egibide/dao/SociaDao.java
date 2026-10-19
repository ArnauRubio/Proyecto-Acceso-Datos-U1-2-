package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Socia;

import java.util.List;

public interface SociaDao {
    Socia getSocia(int id);

    List<Socia> getSocias(int id);

    int addSocia(Socia socia);

    boolean update(Socia socia);

    void delete(int id);

//    List<Socia> getSociasByAsociacionId(int id_asociacion);
}
