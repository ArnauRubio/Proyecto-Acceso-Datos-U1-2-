package org.egibide.repositories;

import org.egibide.dao.AsociacionDao;
import org.egibide.dao.JuegoDao;
import org.egibide.dao.PartidaDao;
import org.egibide.dao.SociaDao;
import org.egibide.idao.AsociacionDaoImpl;
import org.egibide.idao.JuegoDaoImpl;
import org.egibide.idao.PartidaDaoImpl;
import org.egibide.idao.SociaDaoImpl;
import org.egibide.irepositories.JuegoRepository;
import org.egibide.irepositories.SociaRepository;
import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.util.List;

public class SociaRepositoryImpl implements SociaRepository {

    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
    private SociaDao sociaDao = new SociaDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();

    @Override
    public Socia getSocia(int id_socia) {
        Socia socia = sociaDao.getSocia(id_socia);

        Asociacion asociacion = asociacionDao.getAsociacionBySociaId(socia.getId());
        socia.setAsociacion(asociacion);

        List<Partida> partidas = partidaDao.getPartidasBySociaId(socia.getId());
        socia.setPartidas(partidas);

        return socia;
    }
}
