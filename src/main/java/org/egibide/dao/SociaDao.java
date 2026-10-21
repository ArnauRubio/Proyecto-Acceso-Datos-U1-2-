package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;

public interface SociaDao {

    Socia getSocia(int id);

    List<Socia> getSocias();

    int addSocia(Socia socia);

    boolean update(Socia socia);

    void delete(int id);

    Socia getSociaByParticipanteId(int id_participante);

}
