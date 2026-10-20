package org.egibide.dao;

import org.egibide.models.Asociacion;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;

public interface SociaDao {
    Socia getSocia(int id);

    List<Socia> getSocias(int id);

    int addSocia(Socia socia) throws SQLException;

    boolean update(Socia socia, int idOriginal) throws SQLException;

    boolean delete(int id) throws SQLException;

//    List<Socia> getSociasByAsociacionId(int id_asociacion);
}
